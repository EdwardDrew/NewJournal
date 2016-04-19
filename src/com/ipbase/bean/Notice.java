package com.ipbase.bean;

import cn.bmob.v3.BmobObject;

/**
 * @Description：通知消息表
 * @author xianhua
 * @date 2016年3月16日下午5:32:55
 */
@SuppressWarnings("serial")
public class Notice extends BmobObject
{

	private String phone;
	private String noticeContent;

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getNoticeContent()
	{
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent)
	{
		this.noticeContent = noticeContent;
	}

}
