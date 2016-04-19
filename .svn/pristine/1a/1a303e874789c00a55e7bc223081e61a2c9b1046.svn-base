package com.ipbase.fragment;

import org.kymjs.kjframe.ui.KJFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ipbase.ui.SimpleBackActivity;

/**
 * 
 * ClassName: SimpleBackFragment
 * 
 * @Description: SimpleBackActivity对应的Fragment抽象类
 * @author kesar
 * @date 2015-9-2
 */
public abstract class SimpleBackFragment extends KJFragment
{
	// 当前所在的activity的实例
	protected SimpleBackActivity outsideAty;

	@ Override
	public void onCreate( Bundle savedInstanceState )
	{
		if ( getActivity() instanceof SimpleBackActivity )
		{
			outsideAty = (SimpleBackActivity) getActivity();
		}

		super.onCreate( savedInstanceState );
	}

	/** 
	* @Title: setRLVisible 
	* @Description: 设置标题栏是否可见
	* @param @param vi
	* @return void
	* @throws 
	* @auhor 鲜花
	* @date 2015年10月26日
	*/
	public void setRLVisible( int vi )
	{
		if ( outsideAty != null )
		{
			outsideAty.setRLVisible(vi);;
		}
	}
	
	/** 
	* @Title: setRLSearchVisible 
	* @Description: 设置搜索标题栏是否可见
	* @param @param vi
	* @return void
	* @throws 
	* @auhor 鲜花
	* @date 2015年10月26日
	*/
	public void setRLSearchVisible( int vi )
	{
		if ( outsideAty != null )
		{
			outsideAty.setRLSearchVisible(vi);
		}
	}
	
	/**
	 * 
	 * @Description: 设置标题
	 * @param @param text
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-2
	 */
	protected void setTitle( String text )
	{
		if ( outsideAty != null )
		{
			outsideAty.setTitle( text );
		}
	}

	/**
	 * 
	 * @Description: 设置返回键的图标资源
	 * @param @param resId
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-2
	 */
	protected void setBackImage( int resId )
	{
		if ( outsideAty != null )
		{
			outsideAty.setBackImage( resId );
		}
	}
	
	/** 
	* @Title: setBackImageSearch 
	* @Description: 设置搜索标题栏返回键的图标资源
	* @param @param resId
	* @return void
	* @throws 
	* @auhor 鲜花
	* @date 2015年10月26日
	*/
	protected void setBackImageSearch( int resId )
	{
		if ( outsideAty != null )
		{
			outsideAty.setBackImageSearch( resId );
		}
	}

	/**
	 * 
	 * @Description: 得到intent
	 * @param @return
	 * @return Intent
	 * @throws
	 * @author kesar
	 * @date 2015-9-2
	 */
	protected Intent getIntent()
	{
		return outsideAty.getIntent();
	}
	
	/**
	 * 
	 * @Description: 获取Bundle
	 * @param @return   
	 * @return Bundle  
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	protected Bundle getBundleData()
	{
		return outsideAty.getBundleData();
	}

	/**
	 * 
	 * @Description: 当ActionBar上的返回键被按下时
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-2
	 */
	public void onBackClick()
	{
		outsideAty.finish();
	}
}
