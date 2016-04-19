package com.ipbase.fragment;

import java.util.List;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.adapter.NoticeAdapter;
import com.ipbase.bean.Notice;
import com.ipbase.residemenu.ResideMenuItem;
import com.ipbase.widget.NoticeDialog;

/**
 * @Description：通知
 * @author xianhua
 * @date 2016年3月6日下午7:44:18
 */
public class NoticeFragment extends SimpleBackFragment
{

	@BindView(id = R.id.lv_notice)
	private ListView mLvNotice;

	private NoticeAdapter mNoticeAdapter;

	private List<Notice> mNotices;

	private Notice mNotice;

	/*
	 * private List<NoticeDb> mNoticeDbs = null; private NoticeDb mNoticeDb;
	 */

	@Override
	protected View inflaterView(LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle)
	{
		return View.inflate(outsideAty, R.layout.fragment_notice, null);
	}

	@Override
	protected void initData()
	{
		super.initData();
		setTitle("通知");

		// String messageParse = getBundleData().getString("messageParse");
	}

	/**
	 * @Title: queryNoticeTable
	 * @Description: 查询通知表的通知消息
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月16日
	 */
	private void queryNoticeTable()
	{
		BmobQuery<Notice> query = new BmobQuery<Notice>();
		query.addWhereEqualTo("phone", AppContext.getInstance().getUser()
				.getPhone());
		query.findObjects(outsideAty, new FindListener<Notice>()
		{
			@Override
			public void onSuccess(List<Notice> notices)
			{
				// Log.d("通知表查询成功：共", notices.size() + "条数据。");
				mNotices = notices;
				if (notices.size() == 0)
				{
					ViewInject.toast("暂无通知！");
				}

				mNoticeAdapter = new NoticeAdapter(outsideAty, mNotices);
				mLvNotice.setAdapter(mNoticeAdapter);
				// 更新本地通知表
				// updateNoticeDbsTable();
			}

			@Override
			public void onError(int code, String msg)
			{
				/*
				 * List<NoticeDb> noticeDbs = DataBaseUtils.getInstance()
				 * .getNotices(); mNoticeDbs = noticeDbs; if (noticeDbs != null
				 * && noticeDbs.size() > 0) { mNoticeAdapter = new
				 * NoticeAdapter(outsideAty, mNoticeDbs);
				 * mLvNotice.setAdapter(mNoticeAdapter);
				 */

				ViewInject.toast("通知表更新失败，错误码：" + code + "，错误信息：" + msg);
				Log.d("通知表更新失败", "错误码：" + code + "，错误信息：" + msg);
				/*
				 * } else { ViewInject.toast("暂无通知！"); }
				 */
			}
		});
	}

	/**
	 * @Title: updateNoticeDbsTable
	 * @Description: 更新本地通知表
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月18日
	 */
	/*
	 * public void updateNoticeDbsTable() { mNoticeDbs = new
	 * ArrayList<NoticeDb>(); for (Notice notice : mNotices) { mNoticeDb = new
	 * NoticeDb(); mNoticeDb.setObjectId(notice.getObjectId());
	 * mNoticeDb.setPhone(notice.getPhone());
	 * mNoticeDb.setNoticeContent(notice.getNoticeContent());
	 * mNoticeDb.setCreatedAt(notice.getCreatedAt());
	 * mNoticeDb.setUpdatedAt(notice.getUpdatedAt()); mNoticeDbs.add(mNoticeDb);
	 * Log.d("mNoticeDb", mNoticeDb.toString()); }
	 * 
	 * List<NoticeDb> noticeDbs = DataBaseUtils.getInstance().getNotices(); if
	 * (noticeDbs != null && noticeDbs.size() > 0) {
	 * DataBaseUtils.getInstance().removeNotices(noticeDbs); }
	 * 
	 * DataBaseUtils.getInstance().saveNotices(mNoticeDbs); }
	 */

	@Override
	protected void initWidget(View parentView)
	{
		super.initWidget(parentView);

		// 如果ResideMenuItem没初始化，tv_title为null
		if (ResideMenuItem.tv_title == null)
		{
			// 标记显示消息气泡为假，隐藏气泡
			ResideMenuItem.mBadgeViewState = false;
		}
		else
		{
			// 隐藏消息气泡
			ResideMenuItem.hideBadgeView();
		}

		// 查询通知表的通知消息
		queryNoticeTable();

		mLvNotice.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				mNotice = (Notice) mNoticeAdapter.getItem(position);
				// 弹出显示一个消息内容的对话框
				showAlertDialog(mNotice.getNoticeContent());
			}
		});

	}

	/**
	 * @Title: showAlertDialog
	 * @Description: 弹出显示一个消息内容的对话框
	 * @param message
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月16日
	 */
	public void showAlertDialog(String message)
	{
		NoticeDialog.Builder builder = new NoticeDialog.Builder(outsideAty);
		builder.setMessage(message);
		builder.setTitle("消息内容");
		builder.setPositiveButton("删除此通知",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						// 删除选中的通知
						deleteNotice();
						// deleteNoticeDb();
					}
				});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();

					}
				});

		builder.create().show();

	}

	/*
	 * protected void deleteNoticeDb() {
	 * DataBaseUtils.getInstance().removeNotice(mNoticeDb);
	 * mNoticeDbs.remove(mNoticeDb); mNoticeAdapter.notifyDataSetChanged();
	 * ViewInject.toast("通知删除成功"); }
	 */

	/**
	 * @Title: deleteNotice
	 * @Description: 删除选中的通知
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月16日
	 */
	private void deleteNotice()
	{
		Notice notice = new Notice();
		notice.setObjectId(mNotice.getObjectId());
		notice.delete(outsideAty, new DeleteListener()
		{

			@Override
			public void onSuccess()
			{
				ViewInject.toast("通知删除成功");
				mNotices.remove(mNotice);
				mNoticeAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int code, String msg)
			{
				ViewInject.toast("通知删除失败，错误码：" + code + "，错误信息：" + msg);

			}
		});
	}

}
