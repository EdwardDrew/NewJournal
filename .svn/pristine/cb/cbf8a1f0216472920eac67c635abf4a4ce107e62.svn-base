package com.ipbase.utils;

import java.util.List;

import org.json.JSONObject;
import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.ViewInject;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.ipbase.AppApplication;
import com.ipbase.AppContext;
import com.ipbase.bean.User;
import com.ipbase.fragment.LoginFragment;
import com.ipbase.fragment.SimpleBackFragment;
import com.ipbase.ui.MainActivity;

public class UIHelper
{
	private static User mUser = null;

	public static void saveUser( Context cxt, User user )
	{
		KJDB kjdb = KJDB.create( cxt );
		kjdb.deleteByWhere( User.class, "" );
		mUser = user;
		// kjdb.dropDb();
		kjdb.save( user );
	}

	public static User getUser( Context cxt )
	{
		if ( mUser != null )
		{
			return mUser;
		}
		KJDB kjdb = KJDB.create( cxt );
		List< User > datas = kjdb.findAll( User.class );
		if ( datas != null && datas.size() > 0 )
		{
			mUser = datas.get( 0 );
		}
		else
		{
			mUser = new User();
			kjdb.save( mUser );
		}
		return mUser;
	}

	public static void logOut( Context cxt )
	{
		KJDB kjdb = KJDB.create();
		kjdb.delete( mUser );
		mUser = null;
	}

	public static User getUserFromServer( String WEB_PATH, KJHttp kjh,
			String PhoneNum, String Password, final SimpleBackFragment fragment )
	{
		HttpParams params = new HttpParams();
		params.put( "phone", PhoneNum );
		if ( Password != null && !Password.equals( "" ) )
			params.put( "password", Password );

		kjh.get( WEB_PATH, params, new HttpCallBack()
		{
			public void onSuccess( String t )
			{
				super.onSuccess( t );
				// ViewInject.toast( "成功信息:" + t );
				try
				{
					JSONObject object = new JSONObject( t );
					boolean success = object.getBoolean( "result" );
					String userStr = object.getString( "user" );
					ViewInject.toast( userStr );
					// System.out.println( userStr );
					if ( success )
					{
						mUser = JSON.parseObject( userStr, User.class );
						if ( mUser != null )
						{
							ViewInject.toast( mUser.toString() );
							if ( fragment instanceof LoginFragment )
							{
								AppContext.getInstance().setUser( mUser, true );
								MainActivity activity = AppApplication
										.getInstance().getMainActivity();
								if ( activity != null )
								{
									activity.reFresh();
								}
								fragment.onBackClick();
							}
						}
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace();
					ViewInject.toast( "用户名或密码错误" );
					mUser = null;
				}
			};

			@ Override
			public void onFailure( int errorNo, String strMsg )
			{
				super.onFailure( errorNo, strMsg );
				ViewInject.toast( "错误码:" + errorNo + ", 错误信息:" + strMsg );
				mUser = null;
			}
		} );
		return mUser;
	}
}
