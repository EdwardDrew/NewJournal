package com.ipbase.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import com.alibaba.fastjson.JSON;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.adapter.PullAndRefreshAdapter;
import com.ipbase.bean.Comment;
import com.ipbase.bean.User;
import com.ipbase.pulltorefresh.library.PullToRefreshBase;
import com.ipbase.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ipbase.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.ipbase.pulltorefresh.library.PullToRefreshListView;
import com.ipbase.thread.LoadCommentsTask;
import com.ipbase.utils.NCMUtils;
import com.ipbase.widget.MyDialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LookCommentsFragment extends SimpleBackFragment
{
	private String WEB_PATH;
	private String WEB_COMMENTS;
	private String WEB_SUBMIT;
	private KJHttp kjh;

	private String mArticleID;

	private PullAndRefreshAdapter adapter;
	private ArrayList< String > data = null;
	private ArrayList< Comment > commentLists = null;

	@ BindView( id = R.id.pull_refresh_list )
	private PullToRefreshListView refreshListview;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_look_comments, null );
	}

	@ Override
	protected void initData()
	{
		super.initData();

		WEB_PATH = getString( R.string.url_base ) + "/comments/article_id/";
		WEB_COMMENTS = getString( R.string.url_base ) + "/comments/";
		WEB_SUBMIT = getString( R.string.url_base ) + "/comments/";
		kjh = new KJHttp();

		data = new ArrayList< String >();
		commentLists = new ArrayList< Comment >();

		String temp = null;
		temp = getArguments().getString( "ArticleID" );
		if ( temp != null && !temp.equals( "" ) )
		{
			mArticleID = temp;
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
		setTitle( "所有评论" );

		loadCommentListData();
		refreshListview.setMode( Mode.PULL_FROM_START );
		initListener();
	}

	private void loadCommentListData()
	{
		String url = WEB_PATH + mArticleID;
		kjh.get( url, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				// Log.e( "xinyiTag", info );
				doBindAdapterView( info );
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 获取文章评论失败", "错误码：" + errorNo + " ,错误信息："
						+ strMsg );
			};

			public void onFinish()
			{
			};
		} );
	}

	private void doBindAdapterView( String info )
	{
		try
		{
			JSONObject jsonObject = new JSONObject( info );

			boolean result = jsonObject.getBoolean( "result" );
			if ( !result )
			{
				ViewInject.toast( "本文暂无评论！" );
				adapter = new PullAndRefreshAdapter( outsideAty,
						new ArrayList< Comment >(), new ArrayList< String >() );
				refreshListview.setAdapter( adapter );
				return;
			}

			adapter = new PullAndRefreshAdapter( outsideAty, commentLists, data );
			refreshListview.setAdapter( adapter );

			JSONArray jsonArray = jsonObject.getJSONArray( "comments" );

			int size = jsonArray.length();
			for ( int i = 0; i < size; i++ )
			{
				Comment comment = new Comment();
				JSONObject jsonComment = jsonArray.getJSONObject( i );

				comment.setId( jsonComment.getString( "id" ) );
				comment.setUser_id( jsonComment.getString( "user_id" ) );
				comment.setUserName( jsonComment.getString( "nickname" ) );
				comment.setArticle_id( "article_id" );
				comment.setComment_id( jsonComment.getString( "comment_id" ) );
				comment.setContent( jsonComment.getString( "content" ) );
				comment.setType( jsonComment.getInt( "type" ) );
				comment.setCreatetime( jsonComment.getString( "createtime" ) );

				commentLists.add( comment );
				doDealSetCommentsView( comment );
			}
		}
		catch ( JSONException e )
		{
			e.printStackTrace();
		}
	}

	private void doDealSetCommentsView( Comment comment )
	{
		int type = comment.getType();
		if ( type == PullAndRefreshAdapter.COMMENT )
		{
			String userName = comment.getUserName();
			data.add( userName + " 说：" + comment.getContent() );
			adapter.notifyDataSetChanged();
		}
		else if ( type == PullAndRefreshAdapter.REPLY )
		{
			doGetReplyToWhomByCommentID( comment );
		}
	}

	private void doGetReplyToWhomByCommentID( final Comment comment )
	{
		String url = WEB_COMMENTS + comment.getComment_id();
		kjh.get( url, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				// Log.e( "xinyiTag 评论 ID 找对象", info );
				try
				{
					JSONObject jsonObject = new JSONObject( info );
					JSONObject commentObject = jsonObject
							.getJSONObject( "comment" );
					
					String firstPeople = commentObject.getString( "nickname" );
					String str = comment.getUserName() + " 回复 " + firstPeople
							+ " 说：" + comment.getContent();
					data.add( str );
					adapter.notifyDataSetChanged();
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 通过评论 ID 找回复的对象失败", "错误码：" + errorNo
						+ ", 错误原因：" + strMsg );
			};

			public void onFinish()
			{
			};
		} );
	}

	private void initListener()
	{
		refreshListview
				.setOnRefreshListener( new OnRefreshListener< ListView >()
				{
					@ Override
					public void onRefresh(
							PullToRefreshBase< ListView > refreshView )
					{
						// ViewInject.toast( "下拉刷新..." );
						// 获取当前时间
						String label = DateUtils.formatDateTime( outsideAty,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL );

						// 更新上一次刷新的时间
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel( label );

						LoadCommentsTask task = new LoadCommentsTask( kjh,
								WEB_PATH, WEB_COMMENTS, mArticleID,
								refreshListview );
						task.execute( adapter );
					}
				} );

		refreshListview.setOnItemClickListener( new OnItemClickListener()
		{
			@ Override
			public void onItemClick( AdapterView< ? > parent, View view,
					int pos, long id )
			{
				doReplyToWhom( pos - 1 );
			}
		} );
	}

	private void doReplyToWhom( final int pos )
	{
		final ArrayList< Comment > commentsLists = adapter.getCommentLists();
		ArrayList< String > dataLists = adapter.getDataList();

		String toReplyPeople = null;
		String commentID = null;
		String str = dataLists.get( pos );

		if ( str.contains( " 回复 " ) )
		{
			toReplyPeople = str.split( " 回复 " )[0];
		}
		else
		{
			toReplyPeople = str.split( " 说：" )[0];
		}

		int size = commentsLists.size();
		for ( int i = 0; i < size; i++ )
		{
			Comment comment = commentsLists.get( i );
			if ( comment.getUserName().equals( toReplyPeople ) )
			{
				commentID = comment.getId();
				break;
			}
		}
		final String commentID2 = commentID;

		MyDialog myDialog = new MyDialog( outsideAty, "回复 " + toReplyPeople,
				"", false, new MyDialog.onClickOKButtonListener()
				{
					@ Override
					public void back( String returnInfo )
					{
						doSubmitComments( returnInfo, commentID2 );
					}
				}, MyDialog.SELFINPUT );
		myDialog.show();
		NCMUtils.setDialogFillWidth( outsideAty, myDialog );
	}

	private void doSubmitComments( final String replyContent,
			final String commentID )
	{
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
		comments.setContent( replyContent );
		comments.setType( 2 );
		comments.setArticle_id( mArticleID );
		comments.setComment_id( commentID );

		HttpParams params = new HttpParams();
		params.putJsonParams( JSON.toJSONString( comments ) );

		kjh.jsonPost( WEB_SUBMIT, params, false, new HttpCallBack()
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
						refreshListview.setRefreshing();
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

	@ Override
	protected void widgetClick( View view )
	{
		super.widgetClick( view );
	}

};