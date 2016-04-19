package com.ipbase.bean;

/**
 * 
 * ClassName: FeedBack 
 * @Description: 反馈
 * @author kesar
 * @date 2015-10-14
 */
public class FeedBack
{
	private String id;
	private String user_id;
	private String content;

	public String getId()
	{
		return id;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public String getContent()
	{
		return content;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setUser_id(String userId)
	{
		user_id = userId;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	@Override
	public String toString()
	{
		return "FeedBack [content=" + content + ", id=" + id + ", user_id="
				+ user_id + "]";
	}

}
