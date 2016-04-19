package com.ipbase.residemenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ipbase.R;
import com.ipbase.widget.BadgeView;

/**
 * User: special Date: 13-12-10 Time: 下午11:05 Mail: specialcyci@gmail.com
 */
public class ResideMenuItem extends LinearLayout
{
	/** menu item icon */
	private ImageView iv_icon;

	/** menu item title */
	public static TextView tv_title;

	private String mTitle;

	// 消息气泡
	private static BadgeView mBadge;

	// 消息气泡显示的标志,true显示，false不显示
	public static boolean mBadgeViewState = false;

	public ResideMenuItem(Context context)
	{
		super(context);
		initViews(context);
	}

	public ResideMenuItem(Context context, int icon, int title)
	{
		super(context);
		initViews(context);
		iv_icon.setImageResource(icon);
		tv_title.setText(title);
	}

	public ResideMenuItem(Context context, int icon, String title)
	{
		super(context);
		mTitle = title;
		initViews(context);
		iv_icon.setImageResource(icon);
		tv_title.setText(title);
	}

	private void initViews(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.residemenu_item, this);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_title = (TextView) findViewById(R.id.tv_title);

		if (mTitle.equals("通知"))
		{
			mBadge = new BadgeView(context, tv_title);
			mBadge.setText(" ");
			mBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

			if (mBadgeViewState)
			{
				showBadgeView();
			}
			else
			{
				hideBadgeView();
			}
		}

	}

	/**
	 * @Title: showBadgeView
	 * @Description: 通知显示红点气泡
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月17日
	 */
	public static void showBadgeView()
	{
		mBadge.show();
	}

	/**
	 * @Title: hideBageView
	 * @Description: 隐藏红点气泡
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月17日
	 */
	public static void hideBadgeView()
	{
		mBadge.hide();
	}

	/**
	 * set the icon color;
	 * 
	 * @param icon
	 */
	public void setIcon(int icon)
	{
		iv_icon.setImageResource(icon);
	}

	/**
	 * set the title with resource ;
	 * 
	 * @param title
	 */
	public void setTitle(int title)
	{
		tv_title.setText(title);
	}

	/**
	 * set the title with string;
	 * 
	 * @param title
	 */
	public void setTitle(String title)
	{
		tv_title.setText(title);
	}
}
