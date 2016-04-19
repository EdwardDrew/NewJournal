package com.ipbase.adapter;

import java.util.ArrayList;

import com.ipbase.R;
import com.ipbase.bean.Comment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PullAndRefreshAdapter extends BaseAdapter
{
	final static public int COMMENT = 1;
	final static public int REPLY = 2;

	private Activity aty;
	private ArrayList< Comment > commentLists = null;
	private ArrayList< String > data = null;

	public PullAndRefreshAdapter( Activity activity,
			ArrayList< Comment > lists, ArrayList< String > data )
	{
		this.aty = activity;
		this.commentLists = lists;
		this.data = data;
	}

	@ Override
	public int getCount()
	{
		return data == null ? 0 : data.size();
	}

	@ Override
	public Object getItem( int pos )
	{
		return data == null ? null : data.get( pos );
	}

	@ Override
	public long getItemId( int pos )
	{
		return pos;
	}

	public ArrayList< Comment > getCommentLists()
	{
		return commentLists == null ? null : commentLists;
	}

	public void setCommentLists( ArrayList< Comment > lists )
	{
		this.commentLists = lists;
	}

	public void setDataList( ArrayList< String > data )
	{
		this.data = data;
	}

	public ArrayList< String > getDataList()
	{
		return this.data;
	}

	@ SuppressLint( "InflateParams" )
	@ Override
	public View getView( int pos, View convertView, ViewGroup parent )
	{
		ViewHolder holder = null;

		if ( null == convertView )
		{
			holder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from( aty );
			convertView = mInflater.inflate( R.layout.comment_and_reply_item,
					null );

			holder.tv = (TextView) convertView.findViewById( R.id.reply );
			convertView.setTag( holder );
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText( data.get( pos ) );

		return convertView;
	}

	private class ViewHolder
	{
		private TextView tv;
	};

};