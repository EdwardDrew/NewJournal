package com.ipbase.adapter;

import java.util.ArrayList;

import com.ipbase.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 首页年份设配器
 * 
 * @author 李先华 2015年10月1日下午5:40:18
 */
public class YearAdapter extends BaseAdapter
{
	@ SuppressWarnings( "unused" )
	private Activity aty;
	private LayoutInflater inflater;
	private ArrayList< String > yearData;
	private int selectedPosition = -1;

	public YearAdapter( Activity aty, ArrayList< String > yearData )
	{
		this.aty = aty;
		this.yearData = yearData;
		inflater = LayoutInflater.from( aty );
	}

	@ Override
	public int getCount()
	{
		return yearData.size();
	}

	@ Override
	public Object getItem( int position )
	{
		return position;
	}

	@ Override
	public long getItemId( int position )
	{
		return position;
	}

	@ SuppressLint( "InflateParams" )
	@ Override
	public View getView( int pos, View convertView, ViewGroup parent )
	{
		ViewHolder holder = null;
		if ( convertView == null )
		{
			convertView = inflater.inflate( R.layout.year_list_item, null );
			holder = new ViewHolder();
			holder.textView = (TextView) convertView
					.findViewById( R.id.tv_year );
			holder.imageView = (ImageView) convertView
					.findViewById( R.id.iv_year );
			// holder.layout=(LinearLayout)convertView.findViewById(R.id.colorlayout);
			convertView.setTag( holder );
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置选中效果
		if ( selectedPosition == pos )
		{
			holder.textView.setTextColor( Color.RED );
			// holder.layout.setBackgroundColor(Color.LTGRAY);
			holder.imageView.setBackgroundResource( R.drawable.iv_year_select );
			holder.imageView.setVisibility( View.VISIBLE );
			;
		}
		else
		{
			holder.textView.setTextColor( Color.BLACK );
			// holder.layout.setBackgroundColor(Color.TRANSPARENT);
			holder.imageView.setVisibility( View.INVISIBLE );
		}

		holder.textView.setText( yearData.get( pos ) + "年" );
		holder.textView.setTextColor( Color.BLACK );
		// holder.imageView.setBackgroundResource(images[position]);
		return convertView;
	}

	public static class ViewHolder
	{
		public TextView textView;
		public ImageView imageView;
		// public LinearLayout layout;
	}

	public void setSelectedPosition( int position )
	{
		selectedPosition = position;
	}

}
