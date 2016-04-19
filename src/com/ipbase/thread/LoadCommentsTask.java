package com.ipbase.thread;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.ViewInject;

import com.ipbase.adapter.PullAndRefreshAdapter;
import com.ipbase.bean.Comment;
import com.ipbase.pulltorefresh.library.PullToRefreshListView;

import android.os.AsyncTask;
import android.util.Log;

public class LoadCommentsTask extends
		AsyncTask< PullAndRefreshAdapter, Void, String >
{
	private KJHttp kjh;
	private String WEB_PATH;
	private String WEB_COMMENTS;
	private String articleID;
	private String httpInfo;
	private int flag = 0;
	private PullToRefreshListView listView;
	private PullAndRefreshAdapter adapter;

	private ArrayList< Comment > commentLists;
	private ArrayList< String > data;

	public LoadCommentsTask( KJHttp kjh, String url, String url_WEB_COMMENTS,
			String articleID, PullToRefreshListView listView )
	{
		this.kjh = kjh;
		this.WEB_PATH = url;
		this.articleID = articleID;
		this.listView = listView;
		this.WEB_COMMENTS = url_WEB_COMMENTS;

		this.commentLists = new ArrayList< Comment >();
		this.data = new ArrayList< String >();
	}

	@ Override
	protected void onPreExecute()
	{
		super.onPreExecute();
	}

	@ Override
	protected String doInBackground( PullAndRefreshAdapter... params )
	{
		this.adapter = params[0];

		String url = WEB_PATH + articleID;

		kjh.get( url, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				httpInfo = info;
				Log.e( "xinyiTag", info );

				try
				{
					JSONObject jsonObject = new JSONObject( info );
					boolean result = jsonObject.getBoolean( "result" );

					if ( result )
					{
						flag = 1;
					}
					else
					{
						flag = 2;
					}
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 获取文章评论失败", "错误码：" + errorNo + " ,错误信息："
						+ strMsg );
			};

			public void onFinish()
			{
			};
		} );

		while ( true )
		{
			if ( flag == 1 )
			{
				return httpInfo;
			}
			else if ( flag == 2 )
			{
				return "本文暂无评论！";
			}
		}
	}

	@ Override
	protected void onProgressUpdate( Void... values )
	{
		super.onProgressUpdate( values );
	}

	@ Override
	protected void onPostExecute( String info )
	{
		if ( info != null && info.equals( "本文暂无评论！" ) )
		{
			ViewInject.toast( info );
			adapter.setCommentLists( new ArrayList< Comment >() );
			adapter.setDataList( new ArrayList< String >() );
			adapter.notifyDataSetChanged();
			listView.onRefreshComplete();
			super.onPostExecute( info );
			return;
		}

		try
		{
			JSONObject jsonObject = new JSONObject( info );
			JSONArray jsonArray = jsonObject.getJSONArray( "comments" );

			int size = jsonArray.length();
			for ( int i = 0; i < size; i++ )
			{
				Comment comment = new Comment();
				JSONObject jsonComment = jsonArray.getJSONObject( i );

				comment.setId( jsonComment.getString( "id" ) );
				comment.setUser_id( jsonComment.getString( "user_id" ) );
				comment.setUserName( jsonComment.getString( "nickname" ) );
				comment.setArticle_id( "article_id" );
				comment.setComment_id( jsonComment.getString( "comment_id" ) );
				comment.setContent( jsonComment.getString( "content" ) );
				comment.setType( jsonComment.getInt( "type" ) );
				comment.setCreatetime( jsonComment.getString( "createtime" ) );

				commentLists.add( comment );
				doDealSetCommentsView( comment );
			}
			
			listView.onRefreshComplete();
			super.onPostExecute( info );
		}
		catch ( JSONException e )
		{
			e.printStackTrace();
		}
	}

	private void doDealSetCommentsView( Comment comment )
	{
		int type = comment.getType();
		if ( type == PullAndRefreshAdapter.COMMENT )
		{
			String userName = comment.getUserName();
			data.add( userName + " 说：" + comment.getContent() );
			adapter.setDataList( data );
			adapter.setCommentLists( commentLists );
			adapter.notifyDataSetChanged();
		}
		else if ( type == PullAndRefreshAdapter.REPLY )
		{
			doGetReplyToWhomByCommentID( comment );
		}
	}

	private void doGetReplyToWhomByCommentID( final Comment comment )
	{
		String url = WEB_COMMENTS + comment.getComment_id();
		kjh.get( url, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				Log.e( "xinyiTag 评论 ID 找对象", info );

				try
				{
					JSONObject jsonObject = new JSONObject( info );
					JSONObject commentObject = jsonObject
							.getJSONObject( "comment" );

					String firstPeople = commentObject.getString( "nickname" );
					String str = comment.getUserName() + " 回复 " + firstPeople
							+ " 说：" + comment.getContent();
					data.add( str );
					adapter.setDataList( data );
					adapter.setCommentLists( commentLists );
					adapter.notifyDataSetChanged();
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 通过评论 ID 找回复的对象失败", "错误码：" + errorNo
						+ ", 错误原因：" + strMsg );
			};

			public void onFinish()
			{
			};
		} );
	}
};