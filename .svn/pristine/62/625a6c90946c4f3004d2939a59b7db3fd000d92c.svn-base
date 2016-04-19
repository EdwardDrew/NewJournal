package com.ipbase.utils;

import org.kymjs.kjframe.ui.ViewInject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;

import com.ipbase.AppConfig;

import android.app.Activity;

public class BmobUtils
{
	private static Activity mActivity;

	public BmobUtils( Activity outAty )
	{
		mActivity = outAty;
		Bmob.initialize( mActivity, AppConfig.Bmob_Key );
	}

	/**
	 * 创建操作 insertObject
	 * 
	 * @return void
	 * @throws
	 */
	public static void insertObject( final BmobObject obj )
	{
		obj.save( mActivity, new SaveListener()
		{
			@ Override
			public void onSuccess()
			{
				ViewInject.toast( "插入数据成功" );
			}

			@ Override
			public void onFailure( int arg0, String arg1 )
			{
				ViewInject.toast( "插入数据失败" );
			}
		} );
	}
	
	public static void deleteFile( BmobFile bmobFile )
	{
		bmobFile.delete( mActivity, new DeleteListener()
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
};