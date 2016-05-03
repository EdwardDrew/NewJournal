package com.ipbase.widget;

import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.ViewInject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ipbase.R;
import com.ipbase.adapter.QiAdapter;
import com.ipbase.adapter.YearAdapter;
import com.ipbase.bean.FragmentPages;
import com.ipbase.bean.YMWidgetBean;
import com.ipbase.ui.SimpleBackActivity;
import com.ipbase.utils.CommonUtils;
import com.ipbase.utils.Installer;

/**
 * * 年月控件类，可简化MainActivity的代码量
 * 
 * @author xinyi 2015年10月5日上午
 * */
public class YearMonthWidget
{
	private Activity mActivity;
	// 年份listview
	private MultiListView yearListView;
	// 期listview
	private MultiListView qiListView;
	// 年份adapter
	private YearAdapter yearAdapter;
	// 期adapter
	private QiAdapter qiAdapter;
	private String WEB_PATH;
	private KJHttp kjh;

	private ArrayList< String > mYears;
	private ArrayList< String > mQikans[];
	private ArrayList< YMWidgetBean > mData;
	private int mCurPos;

//	static public ProgressDialog DIALOG;

	public YearMonthWidget( Activity outActivity )
	{
		mActivity = outActivity;
//		DIALOG = ViewInject.getprogress( mActivity, "获取文章列表中，请稍候...", false );
		initWidget();
	}

	public void initWidget()
	{
		yearListView = (MultiListView) mActivity
				.findViewById( R.id.yearListView );
		qiListView = (MultiListView) mActivity.findViewById( R.id.qiListView );

		if ( !CommonUtils.hasNetwork( mActivity ) )
		{
			yearListView.setVisibility( View.GONE );
			qiListView.setVisibility( View.GONE );
		}
		mCurPos = 0;
		LoadJournal();
	}

	/**
	 * 二级列表默认选择
	 */
	private void LoadJournal()
	{
		WEB_PATH = mActivity.getString( R.string.url_base )
				+ mActivity.getString( R.string.url_journal );
		kjh = new KJHttp();

		// HttpParams params = new HttpParams();
		kjh.get( WEB_PATH, new HttpCallBack()
		{
			@ Override
			public void onSuccess( String info )
			{
				super.onSuccess( info );
				doLoading( info );
			}

			@ Override
			public void onFailure( int errorNo, String strMsg )
			{
				super.onFailure( errorNo, strMsg );
				yearListView.setVisibility( View.GONE );
				qiListView.setVisibility( View.GONE );
				ViewInject.toast( "获取期刊失败" );
			}

			@ Override
			public void onFinish()
			{
				super.onFinish();
			}
		} );
	}

	private void doLoading( String info )
	{
		try
		{
			JSONObject jsonObject = new JSONObject( info );
			JSONArray jsonArray = jsonObject.getJSONArray( "journals" );

			initYear( jsonArray );
			initMonthTerm();
			initYearListener();
		}
		catch ( JSONException e )
		{
			e.printStackTrace();
		}
	}

	private void initYear( JSONArray jsonArray )
	{
		int size = jsonArray.length();

		mData = new ArrayList< YMWidgetBean >();
		mYears = new ArrayList< String >();
		HashSet< String > yearSet = new HashSet< String >();

		for ( int i = 0; i < size; i++ )
		{
			JSONObject yearObject;
			try
			{
				yearObject = jsonArray.getJSONObject( i );
				mData.add( new YMWidgetBean( yearObject
						.getString( "createtime" ),
						yearObject.getString( "id" ), yearObject
								.getString( "month" ), yearObject
								.getString( "term" ), yearObject
								.getString( "year" ) ) );
				if ( yearSet.add( yearObject.getString( "year" ) ) )
				{
					mYears.add( yearObject.getString( "year" ) );
				}
			}
			catch ( JSONException e )
			{
				e.printStackTrace();
			}
		}
		mYears = Installer.popSort( mYears );
		yearAdapter = new YearAdapter( mActivity, mYears );
		yearListView.setAdapter( yearAdapter );
		yearAdapter.setSelectedPosition( 0 );
		yearAdapter.notifyDataSetInvalidated();
	}

	@ SuppressWarnings( "unchecked" )
	private void initMonthTerm()
	{
		mQikans = new ArrayList[mYears.size()];
		ArrayList< YMWidgetBean > tempMData = (ArrayList< YMWidgetBean >) mData
				.clone();
		for ( int i = 0; i < mQikans.length; i++ )
		{
			String year = mYears.get( i );
			mQikans[i] = new ArrayList< String >();
			for ( int j = 0; j < tempMData.size(); j++ )
			{
				YMWidgetBean bean = tempMData.get( j );
				if ( bean != null && bean.getYear().equals( year ) )
				{
					mQikans[i].add( mYears.get( i ) + "年" + bean.getMonth()
							+ "月第" + bean.getTerm() + "期" );
					tempMData.remove( bean );
					j = -1;
				}
			}
		}
		qiAdapter = new QiAdapter( mActivity, mQikans, 0 );
		qiListView.setAdapter( qiAdapter );
	}

	private void initYearListener()
	{
		yearListView.setOnItemClickListener( new OnItemClickListener()
		{
			@ Override
			public void onItemClick( AdapterView< ? > parent, View view,
					int pos, long id )
			{
				mCurPos = pos;
				yearAdapter.setSelectedPosition( pos );
				yearAdapter.notifyDataSetInvalidated();
				qiAdapter = new QiAdapter( mActivity, mQikans, pos );
				qiListView.setAdapter( qiAdapter );
				qiListView.setOnItemClickListener( new OnItemClickListener()
				{
					@ Override
					public void onItemClick( AdapterView< ? > parent,
							View view, int pos, long id )
					{
						ViewInject.toast( mQikans[mCurPos].get( pos ) );

						String articleUrl = findUrlFromData( mCurPos, pos );
						Bundle bundle = new Bundle();
						bundle.putString( "JournalID", articleUrl );
						bundle.putString( "JournalName",
								(String) qiAdapter.getItem( pos ) );
						SimpleBackActivity.postShowWith( mActivity,
								FragmentPages.ArticleList_Page, bundle );

//						DIALOG.show();
					}
				} );

				/*if ( YearMonthWidget.DIALOG.isShowing() )
				{
					YearMonthWidget.DIALOG.dismiss();
				}*/
			}
		} );
		qiListView.setOnItemClickListener( new OnItemClickListener()
		{
			@ Override
			public void onItemClick( AdapterView< ? > parent, View view,
					int pos, long id )
			{
				ViewInject.toast( mQikans[mCurPos].get( pos ) );

				String articleUrl = findUrlFromData( mCurPos, pos );
				Bundle bundle = new Bundle();
				bundle.putString( "JournalID", articleUrl );
				bundle.putString( "JournalName",
						(String) qiAdapter.getItem( pos ) );
				SimpleBackActivity.postShowWith( mActivity,
						FragmentPages.ArticleList_Page, bundle );

//				DIALOG.show();
			}
		} );

		/*if ( YearMonthWidget.DIALOG.isShowing() )
		{
			YearMonthWidget.DIALOG.dismiss();
		}*/
	}

	private String findUrlFromData( int yearPos, int qikanPos )
	{
		String year = mYears.get( yearPos );
		String month = mQikans[yearPos].get( qikanPos ).substring(
				mQikans[yearPos].get( qikanPos ).indexOf( "年" ) + 1,
				mQikans[yearPos].get( qikanPos ).indexOf( "月" ) );
		String term = mQikans[yearPos].get( qikanPos ).substring(
				mQikans[yearPos].get( qikanPos ).indexOf( "第" ) + 1,
				mQikans[yearPos].get( qikanPos ).indexOf( "期" ) );

		int size = mData.size();
		for ( int i = 0; i < size; i++ )
		{
			YMWidgetBean bean = mData.get( i );
			if ( bean != null && bean.getYear().equals( year )
					&& bean.getMonth().equals( month )
					&& bean.getTerm().equals( term ) )
			{
				return bean.getId();
			}
		}

		return null;
	}

	/**
	 * @return yearListView
	 */
	public MultiListView getYearListView()
	{
		return yearListView;
	}

	/**
	 * @return qiListView
	 */
	public MultiListView getQiListView()
	{
		return qiListView;
	}

	/**
	 * @return yearAdapter
	 */
	public YearAdapter getYearAdapter()
	{
		return yearAdapter;
	}

	/**
	 * @return qiAdapter
	 */
	public QiAdapter getQiAdapter()
	{
		return qiAdapter;
	}
};