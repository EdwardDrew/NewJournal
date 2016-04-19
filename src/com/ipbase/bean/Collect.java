package com.ipbase.bean;

import java.sql.Timestamp;

public class Collect
{
	private String id;
	private String user_id;
	private String article_id;
	private Timestamp createtime;

	public String getId()
	{
		return id;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public String getArticle_id()
	{
		return article_id;
	}

	public Timestamp getCreatetime()
	{
		return createtime;
	}

	public void setId( String id )
	{
		this.id = id;
	}

	public void setUser_id( String user_id )
	{
		this.user_id = user_id;
	}

	public void setArticle_id( String article_id )
	{
		this.article_id = article_id;
	}

	public void setCreatetime( Timestamp createtime )
	{
		this.createtime = createtime;
	}

	@ Override
	public String toString()
	{
		String str = null;
		str = "id:" + id + ", userID:" + user_id + ", articleID:" + article_id
				+ ", createtime:" + createtime;

		return str;
	}
};