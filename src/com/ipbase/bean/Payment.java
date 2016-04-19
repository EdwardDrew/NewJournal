package com.ipbase.bean;

import java.sql.Timestamp;

public class Payment
{
	private String id;
	private String user_id;
	private String journal_id;
	private Timestamp createtime;

	public String getId()
	{
		return id;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public String getJournal_id()
	{
		return journal_id;
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

	public void setJournal_id( String journal_id )
	{
		this.journal_id = journal_id;
	}

	public void setCreatetime( Timestamp createtime )
	{
		this.createtime = createtime;
	}

	@ Override
	public String toString()
	{
		String str = null;
		str = "id:" + id + "  ,userId:" + user_id + ",  journalID:"
				+ journal_id + ",  createTime:" + createtime;
		
		return str;
	}
};