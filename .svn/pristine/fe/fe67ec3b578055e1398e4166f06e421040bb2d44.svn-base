package com.ipbase.fragment;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import cn.smssdk.SMSSDK;

import com.ipbase.R;
import com.ipbase.utils.CommonUtils;
import com.ipbase.utils.SMSCallBack;
import com.ipbase.utils.SMSUtils;

/**
 * 
 * ClassName: FindPasswordFragment
 * 
 * @Description: 找回密码界面
 * @author xinyi
 * @date 2015-10-2
 */
public class ForgetPasswordFragment extends SimpleBackFragment implements
		TextWatcher
{
	@ BindView( id = R.id.find_password_et_phone )
	private EditText mEtPhone;
	@ BindView( id = R.id.find_password_et_verification )
	private EditText mEtVerification;
	@ BindView( id = R.id.find_password_btn_verification, click = true )
	private Button mBtnVerification;
	@ BindView( id = R.id.find_password_btn_next, click = true )
	private Button mBtnNext;
	private SMSUtils smsUtils = null;
	private SMSCallBack smsCallBack;

	private final int Msg_Thread_Runing = 0x1;
	private final int Msg_Thread_Stop = 0x2;
	private Thread mThreadVerification = null;
	private final int mTime = 60;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_forget_password,
				null );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		smsUtils = new SMSUtils( outsideAty, mBtnVerification );
		smsCallBack = new SMSCallBack()
		{
			@ Override
			public void doAfterSubmit()
			{
				DoChangePassword();
			}

			@ Override
			public void doAfterGet( boolean ifIsOKTrue )
			{
				if ( ifIsOKTrue )
				{
					setIsOK( true );
					mEtVerification.setText( "智能通过验证" );
					mEtVerification.setEnabled( false );
				}
				else
				{
					setIsOK( false );
					mEtVerification.setText( "" );
					mEtVerification.setEnabled( true );
				}
			}
		};
		smsUtils.initSMS( smsCallBack );
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		setTitle( "忘记密码" );
		initTextChangedListener();
	}

	@ Override
	protected void widgetClick( View v )
	{
		super.widgetClick( v );
		switch ( v.getId() )
		{
			case R.id.find_password_btn_verification :
				doGetVerification();
				break;
			case R.id.find_password_btn_next :
				doNext();
				break;
		}
	}

	/**
	 * 
	 * @Description: 下一步操作
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void doNext()
	{
		if ( !CommonUtils.hasNetwork( getActivity() ) )
		{
			ViewInject.toast( "请检查网络连接!" );
			return;
		}
		String phone = mEtPhone.getText().toString().trim();
		String verification = mEtVerification.getText().toString().trim();
		if ( StringUtils.isEmpty( phone ) || StringUtils.isEmpty( verification ) )
		{
			ViewInject.toast( "手机号或验证码不能为空" );
		}
		else
		{
			if ( !CommonUtils.isMobileNO( phone ) )
			{
				ViewInject.toast( "手机格式不正确" );
				return;
			}
			if ( smsCallBack.getIsOK() )
			{
				DoChangePassword();
			}
			else
			{
				SMSSDK.submitVerificationCode( "86", phone, verification ); // 提交短信验证码
			}
		}
	}

	/**
	 * 
	 * @Description: 获取验证码操作
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void doGetVerification()
	{
		if ( !CommonUtils.hasNetwork( getActivity() ) )
		{
			ViewInject.toast( "请检查网络连接!" );
			return;
		}
		String phoneNum = mEtPhone.getText().toString().trim();
		if ( StringUtils.isEmpty( phoneNum ) )
		{
			ViewInject.toast( "手机号不能为空" );
			return;
		}
		if ( !CommonUtils.isMobileNO( phoneNum ) )
		{
			ViewInject.toast( "手机格式不正确" );
			return;
		}
		SMSSDK.getVerificationCode( "86", phoneNum );
		mThreadVerification = new Thread( mRunnable );
		mThreadVerification.start();
	}

	@ SuppressLint( "HandlerLeak" )
	private Handler mHandler = new Handler()
	{
		@ Override
		public void handleMessage( Message msg )
		{
			super.handleMessage( msg );
			switch ( msg.what )
			{
				case Msg_Thread_Runing : // 线程运行
					int time = (Integer) msg.obj;
					mBtnVerification.setText( time + "s" );
					mBtnVerification.setEnabled( false );
					break;
				case Msg_Thread_Stop : // 线程停止
					mBtnVerification.setEnabled( true );
					mBtnVerification.setText( R.string.btn_verification );
					break;
				default :
					break;
			}
		}
	};

	/**
	 * 获取验证码的线程
	 */
	private Runnable mRunnable = new Runnable()
	{
		private int time = mTime;

		@ Override
		public void run()
		{
			try
			{
				while ( time != 0 )
				{
					Message message = new Message();
					message.obj = time;
					message.what = SMSUtils.Msg_Thread_Runing;
					smsUtils.getHandler().sendMessage( message );
					Thread.sleep( 1000 );
					time--;
				}
				time = mTime;
				smsUtils.getHandler().sendEmptyMessage(
						SMSUtils.Msg_Thread_Stop );
			}
			catch ( Exception e )
			{
			}
		}
	};

	private void DoChangePassword()
	{
		// Bundle bundle = new Bundle();
		// bundle.putString( "phone", mEtPhone.getText().toString().trim() );
		// FindPasswordFragment findPasswordFragment = new
		// FindPasswordFragment();
		// findPasswordFragment.setArguments( bundle );
		// outsideAty.changeFragment( findPasswordFragment );
		outsideAty.changeFragment( new FindPasswordFragment( mEtPhone.getText()
				.toString().trim() ) );
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
		mEtPhone.addTextChangedListener( this );
		mEtVerification.addTextChangedListener( this );
	}

	/**
	 * 
	 * @Description: 停止线程
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void stopThread()
	{
		if ( mThreadVerification != null && mThreadVerification.isAlive() )
		{
			mThreadVerification.interrupt();
			mThreadVerification = null;
		}

	}

	@ Override
	public void onBackClick()
	{
		stopThread();
		outsideAty.changeFragment( new LoginFragment() );
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
		String phone = mEtPhone.getText().toString().trim();
		String verification = mEtVerification.getText().toString().trim();
		if ( StringUtils.isEmpty( phone ) || StringUtils.isEmpty( verification ) )
		{
			mBtnNext.setEnabled( false );
		}
		else
		{
			mBtnNext.setEnabled( true );
		}
	}

	@ Override
	public void onDestroy()
	{
		// 销毁回调监听接口
		SMSSDK.unregisterAllEventHandler();
		stopThread();
		super.onDestroy();
	}

};