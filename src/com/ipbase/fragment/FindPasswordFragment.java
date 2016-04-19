package com.ipbase.fragment;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.ipbase.R;
import com.ipbase.bean.User;
import com.ipbase.utils.CommonUtils;

/**
 * 
 * ClassName: FindPasswordFragment
 * 
 * @Description: 找回密码
 * @author xinyi
 * @date 2015-10-2
 */
public class FindPasswordFragment extends SimpleBackFragment implements
		TextWatcher
{
	private String WEB_PATH;

	@ BindView( id = R.id.find_pwd_et_password )
	private EditText mEtPassword;
	@ BindView( id = R.id.find_pwd_et_repassword )
	private EditText mEtRePassword;
	@ BindView( id = R.id.find_pwd_btn_commit, click = true )
	private Button mBtnCommit;

	private String mPhone;
	private KJHttp mKjHttp;
	
	public FindPasswordFragment( String phone )
	{
		mPhone = phone;
	}

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_find_password, null );
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		setTitle( "找回密码" );
		initTextChangedListener();
	}

	@ Override
	protected void initData()
	{
		super.initData();
		//mPhone = getBundleData().getString( "phone", "" );
		// 网络连接相关
		WEB_PATH = getString( R.string.url_base )
				+ getString( R.string.url_users );
		mKjHttp = new KJHttp();
	}

	@ Override
	protected void widgetClick( View v )
	{
		super.widgetClick( v );
		switch ( v.getId() )
		{
			case R.id.find_pwd_btn_commit :
				doCommit();
				break;
		}
	}

	/**
	 * 
	 * @Description: 初始化EditText的监听器
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void initTextChangedListener()
	{
		mEtPassword.addTextChangedListener( this );
		mEtRePassword.addTextChangedListener( this );
	}

	/**
	 * 
	 * @Description: 提交改密码操作
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void doCommit()
	{
		if ( !CommonUtils.hasNetwork( getActivity() ) )
		{
			ViewInject.toast( "请检查网络连接!" );
			return;
		}
		
		String newPassword = mEtPassword.getText().toString().trim();
		String newPassword2 = mEtRePassword.getText().toString().trim();
		if ( StringUtils.isEmpty( newPassword ) )
		{
			ViewInject.toast( "新密码不能为空" );
			return;
		}
		if ( StringUtils.isEmpty( newPassword2 ) )
		{
			ViewInject.toast( "确认新密码不能为空" );
			return;
		}
		if ( !newPassword.equals( newPassword2 ) )
		{
			ViewInject.toast( "两次输入的新密码不一致" );
			return;
		}
		// 修改密码
		changePwdNow( newPassword );
	}

	private void changePwdNow( final String newPassword )
	{
		HttpParams params = new HttpParams();

		User user = new User();
		user.setPhone( mPhone );
		user.setPassword( newPassword );
		// 字段过滤器，User为何要继承BmobObject？增加了其他奇怪的字段，导致后台接受不到User
		// 建议不要动com.ipbase.bean这里的字段，增加类来处理，否则后台要改起来很麻烦。
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(
				User.class, "phone", "password" );
		params.putJsonParams( JSON.toJSONString( user, filter ) );

		mKjHttp.jsonPut( WEB_PATH, params, new HttpCallBack()
		{
			@ Override
			public void onSuccess( String t )
			{
				super.onSuccess( t );
				ViewInject.toast( t );
				onBackClick();
			}

			@ Override
			public void onFailure( int errorNo, String strMsg )
			{
				super.onFailure( errorNo, strMsg );
				ViewInject.toast( strMsg );
			}
		} );
	}

	@ Override
	public void beforeTextChanged( CharSequence s, int start, int count,
			int after )
	{
	}

	@ Override
	public void onTextChanged( CharSequence s, int start, int before, int count )
	{
	}

	@ Override
	public void afterTextChanged( Editable s )
	{
		String password = mEtPassword.getText().toString().trim();
		String repassword = mEtRePassword.getText().toString().trim();
		if ( StringUtils.isEmpty( password )
				|| StringUtils.isEmpty( repassword ) )
		{
			mBtnCommit.setEnabled( false );
		}
		else
		{
			mBtnCommit.setEnabled( true );
		}
	}
}
