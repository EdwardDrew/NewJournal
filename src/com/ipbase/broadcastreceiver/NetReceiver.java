package com.ipbase.broadcastreceiver;

import com.ipbase.widget.YearMonthWidget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

public class NetReceiver extends BroadcastReceiver
{
	private Activity mActivity;
	private YearMonthWidget mYearMonthWidget;
	private ConnectivityManager mConnectivityManager;
	private NetworkInfo netInfo;

	public NetReceiver( Activity aty, YearMonthWidget widget )
	{
		mActivity = aty;
		mYearMonthWidget = widget;
	}

	@ Override
	public void onReceive( Context context, Intent intent )
	{
		String action = intent.getAction();
		if ( action.equals( ConnectivityManager.CONNECTIVITY_ACTION ) )
		{
			mConnectivityManager = (ConnectivityManager) context
					.getSystemService( Context.CONNECTIVITY_SERVICE );
			netInfo = mConnectivityManager.getActiveNetworkInfo();
			if ( netInfo != null && netInfo.isAvailable() )
			{
				// 网络连接
				// String name = netInfo.getTypeName();

				if ( netInfo.getType() == ConnectivityManager.TYPE_WIFI
						|| netInfo.getType() == ConnectivityManager.TYPE_ETHERNET
						|| netInfo.getType() == ConnectivityManager.TYPE_MOBILE )
				{
					if ( mYearMonthWidget == null )
						mYearMonthWidget = new YearMonthWidget( mActivity );
					else
					{
						mYearMonthWidget.initWidget();
					}
					mYearMonthWidget.getYearListView().setVisibility(
							View.VISIBLE );
					mYearMonthWidget.getQiListView().setVisibility(
							View.VISIBLE );
				}
			}
			else
			{
				if ( mYearMonthWidget.getYearListView() == null
						|| mYearMonthWidget.getQiListView() == null )
				{
					mYearMonthWidget.getYearListView()
							.setVisibility( View.GONE );
					mYearMonthWidget.getQiListView().setVisibility( View.GONE );
				}
			}
		}
	}
}
