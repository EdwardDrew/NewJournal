package com.ipbase.adapter;

import java.util.ArrayList;

import com.ipbase.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HadBuyedAdapter extends BaseAdapter
{
	private Activity aty;
	private ArrayList< JournalInfo > userJournalIDs = new ArrayList< JournalInfo >();

	public HadBuyedAdapter( Activity aty, ArrayList< JournalInfo > data )
	{
		this.aty = aty;
		this.userJournalIDs = data;
	}

	@ Override
	public int getCount()
	{
		return userJournalIDs == null ? 0 : userJournalIDs.size();
	}

	@ Override
	public Object getItem( int pos )
	{
		return userJournalIDs == null ? null : userJournalIDs.get( pos );
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
		ViewHolder holder = null;

		if ( null == convertView )
		{
			holder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from( aty );
			convertView = mInflater
					.inflate( R.layout.fragment_buyed_item, null );

			holder.title = (TextView) convertView
					.findViewById( R.id.tv_qi_bian_num );
			convertView.setTag( holder );
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		JournalInfo info = userJournalIDs.get( pos );
		holder.title.setText( info.getYear() + " 年 " + info.getMonth() + " 月第 "
				+ info.getTerm() + " 期" );

		return convertView;
	}

	private static class ViewHolder
	{
		public TextView title;
	};

	public static class JournalInfo
	{
		private String mYear;
		private String mMonth;
		private String mTerm;
		private String mJournalID;

		public JournalInfo()
		{
			this.mYear = "";
			this.mMonth = "";
			this.mTerm = "";
			this.mJournalID = "";
		}

		public JournalInfo( String year, String month, String term,
				String journalID )
		{
			this.mYear = year;
			this.mMonth = month;
			this.mTerm = term;
			this.mJournalID = journalID;
		}

		public String getYear()
		{
			return mYear;
		}

		public String getMonth()
		{
			return mMonth;
		}

		public String getTerm()
		{
			return mTerm;
		}

		public String getJournalID()
		{
			return mJournalID;
		}

		public void setYear( String year )
		{
			this.mYear = year;
		}

		public void setMonth( String month )
		{
			this.mMonth = month;
		}

		public void setTerm( String term )
		{
			this.mTerm = term;
		}

		public void setJournalID( String journalID )
		{
			this.mJournalID = journalID;
		}
	};
};