package com.ipbase.adapter;

import java.util.ArrayList;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.ViewInject;

import com.ipbase.R;
import com.ipbase.utils.NCMUtils;
import com.ipbase.widget.MyDialog;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectAdapter extends BaseAdapter
{
	private Activity aty;
	private ArrayList< CollectData > data = null;

	private String WEB_PATH;
	private KJHttp kjh;

	public CollectAdapter( Activity aty, ArrayList< CollectData > data )
	{
		this.aty = aty;
		this.data = data;
		WEB_PATH = aty.getString( R.string.url_base ) + "/collects/";
		kjh = new KJHttp();
	}

	@ Override
	public int getCount()
	{
		return data == null ? null : data.size();
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

	@ Override
	public View getView( int pos, View convertView, ViewGroup parent )
	{
		ViewHolder viewHolder = null;
		if ( convertView == null )
		{
			convertView = View.inflate( aty,
					R.layout.fragment_contents_lv_item, null );

			viewHolder = new ViewHolder();
			viewHolder.tv = (TextView) convertView
					.findViewById( R.id.tv_content_title );
			viewHolder.iv = (ImageView) convertView
					.findViewById( R.id.iv_collect );
			convertView.setTag( viewHolder );
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CollectData collectData = (CollectData) data.get( pos );

		String articleName = collectData.getArticleName();
		viewHolder.tv.setText( articleName );
		viewHolder.iv.setImageResource( R.drawable.icon_heart_red );
		setListener( viewHolder, pos );

		return convertView;
	}

	private void setListener( ViewHolder viewHolder, final int pos )
	{
		viewHolder.iv.setOnClickListener( new OnClickListener()
		{
			@ Override
			public void onClick( View view )
			{
				final CollectData collectData = (CollectData) getItem( pos );
				String articleName = collectData.getArticleName();
				MyDialog myDialog = new MyDialog( aty, "取消收藏", "确定要取消收藏 《"
						+ articleName + "》 吗？", false,
						new MyDialog.onClickOKButtonListener()
						{
							@ Override
							public void back( String returnInfo )
							{
								if ( returnInfo != null )
								{
									if ( returnInfo.equals( "OK" ) )
									{
										doCancelCollect( collectData, pos );
									}
								}
							}
						}, MyDialog.OKCancel );
				myDialog.show();
				NCMUtils.setDialogFillWidth( aty, myDialog );
			}
		} );
	}

	private void doCancelCollect( final CollectData collectData, final int pos )
	{
		String url = WEB_PATH + collectData.getCollectID();
		
		// 取消收藏
		kjh.delete( url, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				ViewInject.toast( "取消成功！" );
				data.remove( pos );
				notifyDataSetChanged();
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 删除收藏出错", "错误码：" + errorNo + ", 错误原因：" + strMsg );
			};

			public void onFinish()
			{
			};
		} );
	}

	private class ViewHolder
	{
		private TextView tv;
		private ImageView iv;
	};

	public static class CollectData
	{
		private String collectID;
		private String articleID;
		private String articleName;
		private String journalID;
		private String journalName;

		public CollectData()
		{
			collectID = "";
			articleID = "";
			articleName = "";
			journalID = "";
			journalName = "";
		}

		public CollectData( String collectID, String articleID,
				String articleName, String journalID, String journalName )
		{
			this.collectID = collectID;
			this.articleID = articleID;
			this.articleName = articleName;
			this.journalID = journalID;
			this.journalName = journalName;
		}
		
		public String getCollectID()
		{
			return collectID;
		}
		
		public void setCollectID( String collectID )
		{
			this.collectID = collectID;
		}

		public String getArticleName()
		{
			return articleName;
		}

		public String getJournalID()
		{
			return journalID;
		}

		public void setArticleName( String articleName )
		{
			this.articleName = articleName;
		}

		public void setJournalID( String journalID )
		{
			this.journalID = journalID;
		}

		public String getArticleID()
		{
			return articleID;
		}

		public String getJournalName()
		{
			return journalName;
		}

		public void setArticleID( String articleID )
		{
			this.articleID = articleID;
		}

		public void setJournalName( String journalName )
		{
			this.journalName = journalName;
		}
	};

};