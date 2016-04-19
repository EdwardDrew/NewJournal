package com.ipbase.adapter;

import com.ipbase.R;

import cn.bmob.v3.datatype.BmobFile;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class YiTouAdapter extends BaseAdapter
{
	private Activity mActivity;
	private ArticleData mData[] = null;

	public YiTouAdapter( Activity aty, ArticleData artData[] )
	{
		mActivity = aty;
		mData = artData;
	}

	@ Override
	public int getCount()
	{
		return mData == null ? 0 : mData.length;
	}

	@ Override
	public Object getItem( int pos )
	{
		return mData == null ? "" : mData[pos];
	}

	@ Override
	public long getItemId( int pos )
	{
		return mData == null ? 0 : pos;
	}

	@ SuppressLint( "InflateParams" )
	@ Override
	public View getView( int pos, View convertView, ViewGroup parent )
	{
		ViewHolder holder = null;

		if ( null == convertView )
		{
			holder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from( mActivity );
			convertView = mInflater.inflate( R.layout.fragment_yitougao_item,
					null );

			holder.title = (TextView) convertView
					.findViewById( R.id.tv_contributed_title );
			holder.articleContent = (TextView) convertView
					.findViewById( R.id.tv_contributed_digest );
			holder.state = (TextView) convertView.findViewById( R.id.tv_state );
			holder.payMoney = (TextView) convertView
					.findViewById( R.id.tv_pay_fee );

			convertView.setTag( holder );
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		ArticleData itemData = (ArticleData) getItem( pos );
		if ( null != itemData )
		{
			holder.title.setText( itemData.getBmobFiles().getFilename()
					.toString() );
			holder.articleContent.setText( "文章内容" );
			switch ( itemData.getArticleStates() )
			{
				case 0 :
					holder.state.setText( "审核中..." );
					holder.payMoney.setVisibility( View.GONE );
					break;
				case 1 :
					holder.state.setText( "审核未通过" );
					holder.payMoney.setVisibility( View.GONE );
					break;
				case 2 :
					holder.state.setText( "审核已通过" );
					holder.payMoney.setVisibility( View.VISIBLE );
					holder.payMoney.setText( "请尽快提交版面费!" );
					break;
				case 3 :
					holder.state.setText( "文章已刊登" );
					holder.payMoney.setVisibility( View.GONE );
					break;
			}
		}

		return convertView;
	}

	private static class ViewHolder
	{
		public TextView title;
		public TextView articleContent;
		public TextView state;
		public TextView payMoney;
	};

	public static class ArticleData
	{
		private BmobFile file = null;
		private int articleState;

		public ArticleData( BmobFile file, int state )
		{
			this.file = file;
			this.articleState = state;
		}

		public BmobFile getBmobFiles()
		{
			return this.file;
		}

		public int getArticleStates()
		{
			return this.articleState;
		}

		public void setBmobFiles( BmobFile file )
		{
			this.file = file;
		}

		public void setArticleStates( int state )
		{
			this.articleState = state;
		}
	};
};