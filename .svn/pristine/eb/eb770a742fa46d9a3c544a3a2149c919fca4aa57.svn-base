package com.ipbase.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ipbase.R;
import com.ipbase.bean.Notice;

/**
 * @Description：通知
 * @author xianhua
 * @date 2016年3月16日下午5:35:06
 */
public class NoticeAdapter extends BaseAdapter
{

	private Context mContext;
	private List<Notice> mNotices;
	private LayoutInflater mlayoutInflater;

	public NoticeAdapter(Context context, List<Notice> mNotices)
	{
		this.mContext = context;
		this.mNotices = mNotices;
		mlayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return mNotices.size();
	}

	public Object getItem(int position)
	{
		return mNotices.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mlayoutInflater.inflate(
					R.layout.fragment_notice_listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvNoticeTitle = (TextView) convertView
					.findViewById(R.id.tv_notice_title);
			viewHolder.tvNoticeTime = (TextView) convertView
					.findViewById(R.id.tv_notice_time);
			viewHolder.tvNoticeContent = (TextView) convertView
					.findViewById(R.id.tv_notice_content);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 赋值
		viewHolder.tvNoticeTitle.setText("系统通知");
		viewHolder.tvNoticeTime.setText(mNotices.get(position).getCreatedAt());
		viewHolder.tvNoticeContent.setText(mNotices.get(position)
				.getNoticeContent());

		return convertView;
	}

	public static class ViewHolder
	{
		public TextView tvNoticeTitle;
		public TextView tvNoticeTime;
		public TextView tvNoticeContent;
	}

}
