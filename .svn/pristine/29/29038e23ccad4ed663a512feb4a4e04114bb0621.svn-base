package com.ipbase.fragment;

import java.io.File;
import java.util.List;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.ipbase.AppApplication;
import com.ipbase.AppConfig;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.bean.Icon;
import com.ipbase.bean.User;
import com.ipbase.cropper.CropperActivity;
import com.ipbase.ui.MainActivity;
import com.ipbase.utils.AddImageUtils;
import com.ipbase.utils.MyImageTools;
import com.ipbase.widget.CircleImage;
import com.ipbase.widget.IconView;
import com.ipbase.widget.MyDialog;

/**
 * 
 * ClassName: PersonFragment
 * 
 * @Description: 个人信息界面
 * @author xinyi
 * @date 2015-10-12
 */
public class PersonFragment extends SimpleBackFragment
{
	private String WEB_PATH;

	private KJBitmap kjb;
	private KJHttp kjh;
	private Icon mIcon;
	private User mUser;
	private String mOldNickName;
	private String email;
	private String mFilepath;
	@ BindView( id = R.id.rl_touxiang, click = true )
	private RelativeLayout mRlTouXiang;
	@ BindView( id = R.id.iv_touxiang, click = true )
	private CircleImage mTouXiang;
	@ BindView( id = R.id.ll_icon )
	private LinearLayout mLinearLayout;
	@ BindView( id = R.id.icon_big, click = true )
	private IconView mIconView;
	@ BindView( id = R.id.rl_account, click = true )
	private RelativeLayout mZhanghao;
	@ BindView( id = R.id.rl_nickname, click = true )
	private RelativeLayout mNickName;
	@ BindView( id = R.id.rl_bind_phone, click = true )
	private RelativeLayout mTel;
	@ BindView( id = R.id.rl_bind_mail, click = true )
	private RelativeLayout mEmail;
	@ BindView( id = R.id.rl_WeChatOrAlipay, click = true )
	private RelativeLayout mWeixin;
	@ BindView( id = R.id.tv_account )
	private TextView tv_account;
	@ BindView( id = R.id.tv_nickname )
	private TextView tv_nickName;
	@ BindView( id = R.id.tv_bind_phone )
	private TextView tv_bind_phone;
	@ BindView( id = R.id.tv_bind_mail )
	private TextView tv_bind_mail;
	@ BindView( id = R.id.tv_WeChatOrAlipay )
	private TextView tv_WeChatOrAlipay;

	final static public int REQUEST_CODE_UPDATE_PHOTO = 3;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_person_center, null );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		if ( getBundleData().getString( "flog" ) != null )
		{
			setTitle( getBundleData().getString( "flog" ) );
		}
		WEB_PATH = getString( R.string.url_base )
				+ getString( R.string.url_users );
		kjb = new KJBitmap();
		kjh = new KJHttp();
		mIcon = new Icon();
		mUser = AppContext.getInstance().getUser();
		mOldNickName = mUser.getNickname() == null ? "" : mUser.getNickname();
		email = mUser.getEmail() == null ? "" : mUser.getEmail();
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		tv_account.setText( mUser.getPhone() );
		tv_nickName.setText( mOldNickName.equals( "" ) ? mUser.getPhone()
				: mOldNickName );
		tv_bind_phone.setText( mUser.getPhone() );
		tv_bind_mail.setText( ( mUser.getEmail() == null || mUser.getEmail()
				.equals( "" ) ) ? "绑定邮箱" : mUser.getEmail() );
		tv_WeChatOrAlipay.setText( ( mUser.getWeixin() == null || mUser
				.getWeixin().equals( "" ) ) ? "绑定微信号" : mUser.getWeixin() );
		refreshIcon( mUser );
	}

	private void refreshIcon( User currentUser )
	{
		if ( currentUser != null )
		{
			mIcon.setPhone( currentUser.getPhone() );
			mIcon.setPassword( currentUser.getPassword() );

			// 保存用户头像缓存的文件夹
			final String iconFolder = Environment.getExternalStorageDirectory()
					+ AppConfig.touXiangPath;
			// 当前用户的头像图片名称
			final String iconName = currentUser.getPhone() + "_icon.png";
			File file = new File( iconFolder + iconName );
			if ( file.exists() )// 如果缓存图片存在
			{
				mTouXiang.setImageBitmap( BitmapFactory.decodeFile( file
						.getAbsolutePath() ) );
			}
			else
			{
				mTouXiang.setImageBitmap( BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_man_black ) );
			}

			BmobQuery< Icon > bmobQuery = new BmobQuery< Icon >();
			bmobQuery.addWhereEqualTo( "mPhone", mUser.getPhone() );
			bmobQuery.findObjects( getActivity(), new FindListener< Icon >()
			{
				@ Override
				public void onSuccess( List< Icon > object )
				{
					for ( Icon icon : object )
					{
						if ( iconName.equals( icon.getIcon().getFilename() ) )
						{
							mIcon = icon;
							BmobFile bmobFile = mIcon.getIcon();
							if ( bmobFile != null )
							{
								String url = bmobFile
										.getFileUrl( getActivity() );
								if ( url != null && !url.equals( "" ) )
								{
									kjb.display( mTouXiang, url );
									kjb.saveImage( getActivity(), url,
											iconFolder + iconName, false,
											new HttpCallBack()
											{
												@ Override
												public void onSuccess(
														Bitmap bitmap )
												{
													super.onSuccess( bitmap );
												}
											} );
								}
							}
							break;
						}
					}
				}

				@ Override
				public void onError( int code, String msg )
				{
				}
			} );
		}
	}

	@ Override
	protected void widgetClick( View v )
	{
		switch ( v.getId() )
		{
			case R.id.rl_touxiang :
				AddImageUtils.start( outsideAty, this );
				break;
			case R.id.iv_touxiang :
				SeeBigPhoto();
				break;

			case R.id.rl_account :
				ViewInject.toast( "不允许更换绑定手机，如有问题，请向后台管理员联系" );
				break;

			case R.id.rl_nickname :
				showChangeNickNameDialog();
				break;

			case R.id.rl_bind_phone :
				ViewInject.toast( "不允许更换绑定手机，如有问题，请向后台管理员联系" );
				break;

			case R.id.rl_bind_mail :
				showBindEmail();
				break;

			case R.id.rl_WeChatOrAlipay :
				break;

			default :
				break;
		}
	}

	private void SeeBigPhoto()
	{
		final String iconName = mIcon.getPhone() + "_icon.png";
		File file = new File( Environment.getExternalStorageDirectory()
				+ AppConfig.touXiangPath + iconName );
		if ( file.exists() )// 如果缓存图片存在
		{
			mIconView.setImageBitmap( BitmapFactory.decodeFile( file
					.getAbsolutePath() ) );
		}
		else
		{
			mIconView.setImageBitmap( BitmapFactory.decodeResource(
					getResources(), R.drawable.icon_man_white ) );
		}
		mIconView.setVisibility( View.VISIBLE );
		mLinearLayout.setVisibility( View.VISIBLE );
	}

	private void showChangeNickNameDialog()
	{
		String name = mOldNickName.equals( "" ) ? mUser.getPhone()
				: mOldNickName;
		MyDialog dialog = new MyDialog( getActivity(), "新名字", name, true,
				new MyDialog.onClickOKButtonListener()
				{
					@ Override
					public void back( final String newNickName )
					{
						HttpParams params = new HttpParams();						
						SimplePropertyPreFilter filter = new SimplePropertyPreFilter(
								User.class, "phone", "nickname" );
						params.putJsonParams( JSON.toJSONString( mUser, filter ) );

						Log.e( "xinyiTag", WEB_PATH );
						kjh.jsonPut( WEB_PATH, params, new HttpCallBack()
						{
							@ Override
							public void onSuccess( String t )
							{
								super.onSuccess( t );
								ViewInject.toast( t );
								mOldNickName = newNickName;
								tv_nickName.setText( newNickName );
								mUser.setNickname( newNickName );
								AppContext.getInstance().updateUser( mUser );
							}

							@ Override
							public void onFailure( int errorNo, String strMsg )
							{
								super.onFailure( errorNo, strMsg );
								mUser.setNickname( mOldNickName );
								ViewInject.toast( "网络请求失败:error:" + errorNo
										+ ", strMsg:" + strMsg );
							}
						} );
					}
				} );
		dialog.show();
		setDialogFillWidth( dialog );
	}

	private void showBindEmail()
	{
		MyDialog dialog = new MyDialog( getActivity(), "绑定邮箱", email, false,
				new MyDialog.onClickOKButtonListener()
				{
					@ Override
					public void back( final String newEmail )
					{
						// findIfUserExistInBmob( newEmail );
					}
				} );
		dialog.show();
		setDialogFillWidth( dialog );
	}

	// private void findIfUserExistInBmob( final String newEmail )
	// {
	// BmobQuery< BmobUser > query = new BmobQuery< BmobUser >();
	// query.addWhereEqualTo( "phone", mUser.getPhone() );
	// query.findObjects( getActivity(), new FindListener< BmobUser >()
	// {
	// public void onSuccess( java.util.List< BmobUser > bmobUser )
	// {
	// if ( bmobUser.size() == 1 )
	// {
	// bmobUser.get( 0 ).setEmail( newEmail );
	// bmobUser.get( 0 ).setEmailVerified( true );
	// bmobUser.get( 0 ).update( getActivity(),
	// new UpdateListener()
	// {
	// @ Override
	// public void onSuccess()
	// {
	// sendEmail( newEmail );
	// }
	//
	// @ Override
	// public void onFailure( int errNo, String msg )
	// {
	// }
	// } );
	// }
	// };
	//
	// public void onError( int errNo, String msg )
	// {
	// };
	// } );
	// }
	//
	// private void sendEmail( final String newEmail )
	// {
	// BmobUser.requestEmailVerify( getActivity(), newEmail,
	// new EmailVerifyListener()
	// {
	// @ Override
	// public void onSuccess()
	// {
	// ViewInject
	// .toast( "请求验证邮件成功，请到" + newEmail + "邮箱中进行激活。" );
	// }
	//
	// @ Override
	// public void onFailure( int code, String e )
	// {
	// ViewInject.toast( "请求验证邮件失败:" + e );
	// }
	// } );
	// }

	@ Override
	public void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult( requestCode, resultCode, data );
		if ( resultCode == Activity.RESULT_OK )
		{
			switch ( requestCode )
			{
				case AddImageUtils.REQUEST_CODE_ALBUM :
					loadAlbum( data );
					break;
				case AddImageUtils.REQUEST_CODE_CAIJIANZHAOPIAN :
					cutThePhoto();
					break;
				case REQUEST_CODE_UPDATE_PHOTO :
					updatePhoto( data );
					break;
			}
		}
	}

	private void loadAlbum( Intent data )
	{
		Uri uri = data.getData();
		String tmppath = null;
		if ( uri != null )
		{
			String path = uri.getPath();
			if ( path.startsWith( "/storage" ) )
			{
				tmppath = path;
			}
			else
			{
				tmppath = FileUtils.uri2File( outsideAty, uri )
						.getAbsolutePath();
			}
			Intent intent = new Intent( outsideAty, CropperActivity.class );
			intent.putExtra( "path", tmppath );
			startActivityForResult( intent, REQUEST_CODE_UPDATE_PHOTO );
		}
	}

	private void cutThePhoto()
	{
		File file = AddImageUtils.getFile();
		if ( file != null && file.exists() )
		{
			Bitmap bitmap = MyImageTools.compressImageFromFile( file.getPath() );
			MyImageTools.saveToSdCard( bitmap, AppConfig.imgCachePath,
					"image.png" );
			Intent intent = new Intent( outsideAty, CropperActivity.class );
			intent.putExtra( "path", file.getPath() );
			startActivityForResult( intent, REQUEST_CODE_UPDATE_PHOTO );
		}
	}

	private void updatePhoto( Intent data )
	{
		mFilepath = data.getStringExtra( "imagePath" );
		kjb.display( mTouXiang, mFilepath );

		Bitmap icon = BitmapFactory.decodeFile( mFilepath );
		String icon_path = MyImageTools.saveToSdCard( icon,
				AppConfig.touXiangPath, mIcon.getPhone() + "_icon.png" );
		File waitForUploadFile = new File( icon_path );

		final BmobFile bmobFile = new BmobFile( waitForUploadFile );
		bmobFile.upload( getActivity(), new UploadFileListener()
		{
			@ Override
			public void onSuccess()
			{
				if ( mIcon.getIcon() == null )
				{
					mIcon.setIcon( bmobFile );
					mIcon.save( getActivity() );
				}
				else
				{
					mIcon.getIcon().delete( getActivity() );
					mIcon.setIcon( bmobFile );
					mIcon.update( getActivity(), mIcon.getObjectId(),
							new UpdateListener()
							{
								@ Override
								public void onSuccess()
								{
								}

								@ Override
								public void onFailure( int arg0, String arg1 )
								{
								}
							} );
				}
				ViewInject.toast( "头像修改成功" );
			}

			@ Override
			public void onFailure( int statuscode, String errormsg )
			{
				ViewInject.toast( "修改头像失败，请稍后再试" );
			}
		} );
	}

	@ Override
	public void onBackClick()
	{
		if ( mIconView != null && mLinearLayout != null )
		{
			if ( mIconView.getVisibility() == View.VISIBLE
					|| mLinearLayout.getVisibility() == View.VISIBLE )
			{
				mIconView.setVisibility( View.GONE );
				mLinearLayout.setVisibility( View.GONE );
			}
			else
			{
				MainActivity activity = AppApplication.getInstance()
						.getMainActivity();
				if ( activity != null )
				{
					activity.reFresh();
				}
				super.onBackClick();
			}
		}
	}

	@ SuppressWarnings( "deprecation" )
	private void setDialogFillWidth( MyDialog dialog )
	{
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) ( getActivity().getWindowManager().getDefaultDisplay()
				.getWidth() ); // 设置宽度
		dialog.getWindow().setAttributes( lp );
	}
};