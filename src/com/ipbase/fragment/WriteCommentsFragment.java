package com.ipbase.fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.bean.Comment;
import com.ipbase.bean.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class WriteCommentsFragment extends SimpleBackFragment
{
	private String WEB_PATH;
	private KJHttp kjh;
	private String mArticleID = null;

	@ BindView( id = R.id.ed_write_comment )
	private EditText mEdit_WriteComments;
	@ BindView( id = R.id.btn_submit_comments, click = true )
	private Button mSubmitComments;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View
				.inflate( outsideAty, R.layout.fragment_write_comments, null );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		WEB_PATH = getString( R.string.url_base ) + "/comments/";
		kjh = new KJHttp();
		getParaData();
	}

	private void getParaData()
	{
		/* 获取数据 */
		Bundle bundle = getArguments();
		if ( bundle != null )
		{
			String temp = bundle.getString( "ArticleID" );
			if ( temp != null && !temp.equals( "" ) )
			{
				mArticleID = temp;
			}
			else
			{
				mArticleID = "";
			}
		}
		else
		{
			mArticleID = "";
		}
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		setTitle( "说点什么..." );
	}

	@ Override
	protected void widgetClick( View view )
	{
		super.widgetClick( view );

		switch ( view.getId() )
		{
			case R.id.btn_submit_comments :
				sendComments();
				break;

			default :
				break;
		}
	}

	private void sendComments()
	{
		String input = mEdit_WriteComments.getText().toString();
		if ( StringUtils.isEmpty( input ) )
		{
			ViewInject.toast( "输入内容后再提交" );
			return;
		}
		final ProgressDialog dialog = ViewInject.getprogress( outsideAty,
				"请稍候...", false );
		dialog.show();

		User user = AppContext.getInstance().getUser();
		if ( user == null || mArticleID == null || mArticleID.equals( "" ) )
		{
			ViewInject.toast( "未知错误，请重新登录后再试!" );
			return;
		}

		Comment comments = new Comment();
		comments.setUser_id( user.getId() );
		comments.setContent( input );
		comments.setType( 1 );
		comments.setArticle_id( mArticleID );
		comments.setComment_id( mArticleID );

		HttpParams params = new HttpParams();
		params.putJsonParams( JSON.toJSONString( comments ) );

		Log.e( "xinyiTag", "web_path:" + WEB_PATH );

		kjh.jsonPost( WEB_PATH, params, false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				Log.e( "xinyiTag", info );
				try
				{
					JSONObject jsonObject = new JSONObject( info );
					boolean result = jsonObject.getBoolean( "result" );
					if ( result )
					{
						if ( dialog.isShowing() )
						{
							dialog.dismiss();
						}
						ViewInject.toast( "评论成功！" );
						outsideAty.finish();
					}
					else
					{
						if ( dialog.isShowing() )
						{
							dialog.dismiss();
						}
						ViewInject.toast( "未知错误，请稍后再试！" );
					}
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				if ( dialog.isShowing() )
				{
					dialog.dismiss();
				}
				Log.e( "xinyiTag", "错误码:" + errorNo + ", 错误信息:" + strMsg );
				ViewInject.toast( "未知错误，请稍后再试！" );
			};

			public void onFinish()
			{
			};

		} );
	}
};