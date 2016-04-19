package com.ipbase.bean;

import org.kymjs.kjframe.database.annotate.Id;

/**
 * @Description：同步表，noticeSyncState：sqlite与Bmob后台数据库同步状态，1为离线，同步；2为上网，不同步
 * @author xianhua
 * @date 2016年3月18日下午9:03:20
 */
public class SyncDb
{
	@Id
	private String id;
	private int noticeSyncState;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getNoticeSyncState()
	{
		return noticeSyncState;
	}

	public void setNoticeSyncState(int noticeSyncState)
	{
		this.noticeSyncState = noticeSyncState;
	}

	@Override
	public String toString()
	{
		return "SyncDb [id = " + id + ", noticeSyncState=" + noticeSyncState
				+ "]";
	}

}
