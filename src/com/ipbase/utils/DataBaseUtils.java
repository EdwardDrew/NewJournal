package com.ipbase.utils;

import java.util.List;
import org.kymjs.kjframe.KJDB;
import com.ipbase.bean.NoticeDb;
import com.ipbase.bean.User;
import android.content.Context;

/**
 * 
 * ClassName: DataBaseUtils
 * 
 * @Description: 数据库工具类
 * @author kesar
 * @date 2015年9月23日
 */
public class DataBaseUtils
{
	private KJDB mKjdb;
	private static DataBaseUtils mDataBaseUtils;

	private DataBaseUtils()
	{
	}

	/**
	 * 
	 * @Description: 获取当前数据库工具类实例
	 * @param @return
	 * @return DataBaseUtils
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	public static DataBaseUtils getInstance()
	{
		if (mDataBaseUtils == null)
		{
			mDataBaseUtils = new DataBaseUtils();
		}
		return mDataBaseUtils;
	}

	/**
	 * 
	 * @Description: 初始化得到kjdb
	 * @param @param context
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-9
	 */
	public void init(Context context)
	{
		mKjdb = KJDB.create(context);
	}

	public KJDB getmKjdb()
	{
		return mKjdb;
	}

	/**
	 * 
	 * @Description: 保存用户
	 * @param @param user
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	public void saveUser(User user)
	{
		mKjdb.save(user);
	}

	/**
	 * 
	 * @Description: 从数据库中获取用户信息
	 * @param @return
	 * @return User
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	public User getUser()
	{
		List<User> users = mKjdb.findAll(User.class);
		if (users != null && !users.isEmpty())
		{
			return users.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @Description: 从本地数据库中删除用户数据
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	public void removeUser(User user)
	{
		if (user != null)
		{
			mKjdb.delete(user);
		}
	}

	/**
	 * 
	 * @Description: 更新用户
	 * @param @param user
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	public void updateUser(User user)
	{
		if (user != null)
		{
			mKjdb.update(user);
		}
	}

	/************** 通知相关 ****************/
	/**
	 * @Title: saveNotice
	 * @Description: 保存通知
	 * @param notice
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月17日
	 */
	public void saveNotices(List<NoticeDb> noticeDbs)
	{
		if (noticeDbs != null && noticeDbs.size() > 0)
		{
			mKjdb.save(noticeDbs);
			/*
			 * for (NoticeDb noticeDb : noticeDbs) { mKjdb.save(noticeDb); }
			 */
		}
	}

	/**
	 * @Title: getNotices
	 * @Description: 获取通知
	 * @return
	 * @return List<NoticeDb>
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月18日
	 */
	public List<NoticeDb> getNotices()
	{
		List<NoticeDb> noticeDbs = mKjdb.findAll(NoticeDb.class);
		if (noticeDbs != null && noticeDbs.size() > 0)
		{
			return noticeDbs;
		}
		return null;
	}

	/**
	 * @Title: removeNotices
	 * @Description: 删除多条通知
	 * @param noticeDbs
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月18日
	 */
	public void removeNotices(List<NoticeDb> noticeDbs)
	{
		for (NoticeDb noticeDb : noticeDbs)
		{
			mKjdb.delete(noticeDb);
		}
	}

	/**
	 * @Title: removeNotice
	 * @Description: 删除一条通知
	 * @param noticeDb
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月18日
	 */
	public void removeNotice(NoticeDb noticeDb)
	{
		mKjdb.delete(noticeDb);
	}

}
