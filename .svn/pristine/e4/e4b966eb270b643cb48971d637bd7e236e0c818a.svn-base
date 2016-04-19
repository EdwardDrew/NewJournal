package com.ipbase.adapter;

import java.util.ArrayList;

import com.ipbase.R;
import com.ipbase.utils.Installer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 首页期设配器
 * 
 * @author 李先华 2015年10月1日下午7:15:53
 */
public class QiAdapter extends BaseAdapter
{
	@ SuppressWarnings( "unused" )
	private Activity aty;
	private LayoutInflater layoutInflater;
	private ArrayList< String > qiKans[];
	private int currentPos;

	public QiAdapter( Activity aty, ArrayList< String > qies[], int pos )
	{
		this.aty = aty;
		this.qiKans = qies;
		this.layoutInflater = (LayoutInflater) aty
				.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		this.currentPos = pos;
		doQiKanSort();
	}

	@ Override
	public int getCount()
	{
		return qiKans[currentPos].size();
	}

	@ Override
	public Object getItem( int pos )
	{
		return qiKans[currentPos].get( pos );
	}

	@ Override
	public long getItemId( int pos )
	{
		return pos;
	}

	@ SuppressLint( "InflateParams" )
	@ Override
	public View getView( int pos, View convertView, ViewGroup parent )
	{
		ViewHolder viewHolder = null;

		if ( convertView == null )
		{
			convertView = layoutInflater.inflate( R.layout.qi_list_item, null );
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView
					.findViewById( R.id.tv_qi );
			convertView.setTag( viewHolder );
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textView.setText( qiKans[currentPos].get( pos ) );
		viewHolder.textView.setTextColor( Color.BLACK );

		return convertView;
	}

	public static class ViewHolder
	{
		public TextView textView;
	}

	private void doQiKanSort()
	{
		for ( int i = 0; i < qiKans.length; i++ )
		{
			qiKans[i] = Installer.popSortQiKan( qiKans[i] );
		}
	}
};