package com.ipbase.bean;

import android.content.Context;
import cn.bmob.v3.BmobInstallation;

/**
 * @Description：自定义Installation表
 * @author xianhua
 * @date 2016年3月15日下午9:51:54
 */
public class MyBmobInstallation extends BmobInstallation
{

	private static final long serialVersionUID = 2997064306845247077L;

	/**
	 * 用户id-这样可以将设备与用户之间进行绑定
	 */
	private String uid;

	public MyBmobInstallation(Context context)
	{
		super(context);
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

}