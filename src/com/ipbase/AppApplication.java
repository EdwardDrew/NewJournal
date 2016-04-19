package com.ipbase;

import org.kymjs.kjframe.http.HttpConfig;

import com.ipbase.ui.MainActivity;
import com.ipbase.utils.DataBaseUtils;

import android.app.Application;
import android.content.Context;

/**
 * 
 * ClassName: AppApplication
 * 
 * @Description: App的Application
 * @author kesar
 * @date 2015年9月23日
 */
public class AppApplication extends Application
{
	public Context applicationContext;
	private static AppApplication instance;
	private MainActivity mMainActivity;
	
	@ Override
	public void onCreate()
	{
		super.onCreate();

		applicationContext = this;
		instance = this;
		// 初始化kj框架的缓存
		//BitmapConfig.CACHEPATH = AppConfig.imgCachePath;
		HttpConfig.CACHEPATH = AppConfig.httpCachePath;
		// 初始化数据库工具类
		DataBaseUtils.getInstance().init( applicationContext );
	}

	/**
	 * 
	 * @Description: 获取当前application的实例
	 * @param @return
	 * @return AppApplication
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	public static AppApplication getInstance()
	{
		return instance;
	}

	public MainActivity getMainActivity()
	{
		return mMainActivity;
	}

	public void setMainActivity( MainActivity mMainActivity )
	{
		this.mMainActivity = mMainActivity;
	}
}
