package com.ipbase.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.ViewInject;

import com.alibaba.fastjson.JSON;
import com.ipbase.R;
import com.ipbase.bean.Collect;
import com.ipbase.utils.NCMUtils;
import com.ipbase.widget.MyDialog;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description：CatalogAdapter
 * @author xianhua
 * @date 2016年4月26日上午11:50:10
 */
public class CatalogAdapter extends BaseAdapter
{
	private static final String TAG = "CatalogAdapter";
	
	final static public int NOCOLLECTED = 0;
	final static public int HASCOLLECTED = 1;

	private Activity aty;
	private CatalogItem items[];
	private String userID;

	public CatalogAdapter( Activity aty, CatalogItem items[], String userID )
	{
		this.aty = aty;
		this.items = items;
		this.userID = userID;
	}

	@ Override
	public int getCount()
	{
		return items == null ? 0 : items.length;
	}

	@ Override
	public Object getItem( int pos )
	{
		return items == null ? null : items[pos];
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

		viewHolder.tv.setText( items[pos].getTitle() );
		checkIfHasCollected( viewHolder, pos );

		return convertView;
	}

	private void checkIfHasCollected( final ViewHolder viewHolder, final int pos )
	{
		KJHttp kjh = new KJHttp();
		String WEB_PATH = aty.getString( R.string.url_base )
				+ "/collects/user_id/";

		WEB_PATH = WEB_PATH + userID;

		kjh.jsonGet( WEB_PATH, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				// Log.e( "xinyiTag", info );
				try
				{
					JSONObject jsonObject = new JSONObject( info );
					boolean result = jsonObject.getBoolean( "result" );

					if ( result )
					{
						visitCollectTable( jsonObject, viewHolder, pos );
					}
					else
					{
						viewHolder.iv.setTag( NOCOLLECTED );
						setImageViewListener( viewHolder, items[pos], "" );
					}
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 加载收藏列表失败", "错误码：" + errorNo + " ,错误信息："
						+ strMsg );
			};

			public void onFinish()
			{
			};
		} );
	}

	private void visitCollectTable( JSONObject jsonObject,
			ViewHolder viewHolder, int pos )
	{
		JSONArray jsonArray;
		try
		{
			jsonArray = jsonObject.getJSONArray( "collects" );

			int size = jsonArray.length();
			String collecteID = null;

			for ( int i = 0; i < size; i++ )
			{
				JSONObject collects = jsonArray.getJSONObject( i );
				String articleID = collects.getString( "article_id" );
				collecteID = collects.getString( "id" );

				if ( articleID.equals( items[pos].getArticle_id() ) )
				{
					// Log.e( "xinyiTag", "设置红色" );
					viewHolder.iv.setImageResource( R.drawable.icon_heart_red );
					viewHolder.iv.setTag( HASCOLLECTED );
					setImageViewListener( viewHolder, items[pos], collecteID );

					return;
				}
			}

			// Log.e( "xinyiTag", "设置白色" );
			viewHolder.iv.setImageResource( R.drawable.icon_heart );
			viewHolder.iv.setTag( NOCOLLECTED );
			setImageViewListener( viewHolder, items[pos], collecteID );
		}
		catch ( JSONException e )
		{
			e.printStackTrace();
		}
	}

	private void setImageViewListener( final ViewHolder viewHolder,
			final CatalogItem item, final String collectID )
	{
		viewHolder.iv.setOnClickListener( new View.OnClickListener()
		{
			@ Override
			public void onClick( View view )
			{
				int tag = (Integer) viewHolder.iv.getTag();
				if ( tag == HASCOLLECTED )
				{
					// 已经收藏则取消收藏
					if ( collectID.equals( "" ) )
					{
						doCancelCollected( item.getTitle(), viewHolder,
								item.getCollect_id() );
					}
					else
					{
						doCancelCollected( item.getTitle(), viewHolder,
								collectID );
					}
				}
				else if ( tag == NOCOLLECTED )
				{
					// 还没有收藏则添加收藏
					doCollect( item, viewHolder );
				}
			}
		} );
	}

	private void doCancelCollected( final String articleName,
			final ViewHolder viewHolder, final String collectID )
	{
		// 已经收藏了
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
								doCancelCollect( viewHolder, collectID );
							}
						}
					}
				}, MyDialog.OKCancel );
		myDialog.show();
		NCMUtils.setDialogFillWidth( aty, myDialog );
	}

	private void doCancelCollect( final ViewHolder viewHolder,
			final String collectID )
	{
		String url = aty.getString( R.string.url_base ) + "/collects/"
				+ collectID;
		KJHttp kjh = new KJHttp();

		// 取消收藏
		kjh.delete( url, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				Log.e( "xinyiTag", "取消收藏成功" );
				viewHolder.iv.setImageResource( R.drawable.icon_heart );
				viewHolder.iv.setTag( NOCOLLECTED );
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

	private void doCollect( final CatalogItem item, final ViewHolder viewHolder )
	{
		String url = aty.getString( R.string.url_base ) + "/collects/";
		KJHttp kjh = new KJHttp();

		HttpParams params = new HttpParams();
		Collect collect = new Collect();
		collect.setUser_id( userID );
		collect.setArticle_id( item.getArticle_id() );
		params.putJsonParams( JSON.toJSONString( collect ) );

		kjh.jsonPost( url, params, false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				Log.e( "xinyiTag", "收藏 " + item.getArticle_id() + "成功" );

				try
				{
					JSONObject jsonObject = new JSONObject( info );

					String collectID = jsonObject.getString( "collectid" );
					item.setCollect_id( collectID );

					ViewInject.toast( "收藏成功！" );
					viewHolder.iv.setImageResource( R.drawable.icon_heart_red );
					viewHolder.iv.setTag( HASCOLLECTED );
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 收藏" + item.getArticle_id() + "失败", "失败信息："
						+ errorNo + ", 失败原因：" + strMsg );
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
	}

	public static class CatalogItem
	{
		private String title;
		private String author;
		private String digest;
		private String article_id;
		private String collect_id;
		private String journal_id;
		
		public CatalogItem( String title, String author, String digest,
				String article_id )
		{
			this.title = title;
			this.author = author;
			this.digest = digest;
			this.article_id = article_id;
			this.collect_id = "";
		}
		
		public CatalogItem(String title, String author, String digest,
				String article_id, String journal_id)
		{
			this.title = title;
			this.author = author;
			this.digest = digest;
			this.article_id = article_id;
			this.collect_id = "";
			this.journal_id = journal_id;
		}

		public String getCollect_id()
		{
			return collect_id;
		}

		public void setCollect_id( String collect_id )
		{
			this.collect_id = collect_id;
		}

		public String getTitle()
		{
			return title;
		}

		public String getAuthor()
		{
			return author;
		}

		public String getDigest()
		{
			return digest;
		}

		public String getArticle_id()
		{
			return article_id;
		}

		public void setTitle( String title )
		{
			this.title = title;
		}

		public void setAuthor( String author )
		{
			this.author = author;
		}

		public void setDigest( String digest )
		{
			this.digest = digest;
		}

		public void setArticle_id( String article_id )
		{
			this.article_id = article_id;
		}

		public String getJournal_id()
		{
			return journal_id;
		}

		public void setJournal_id(String journal_id)
		{
			this.journal_id = journal_id;
		}
	};
};