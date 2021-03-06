package com.ipbase.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.adapter.HadBuyedAdapter;
import com.ipbase.adapter.HadBuyedAdapter.JournalInfo;
import com.ipbase.bean.User;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * ClassName: HadBuyedFragment
 * 
 * @Description: “已购买”界面
 * @author xinyi
 * @date 2016-02-28
 */
public class HadBuyedFragment extends SimpleBackFragment
{
	private String WEB_PATH;
	private KJHttp kjh;

	@ BindView( id = R.id.lv_buyed )
	private ListView listView;
	private HadBuyedAdapter adapter;

	private String userID;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_buyed, null );
	}

	@ Override
	protected void initData()
	{
		super.initData();

		WEB_PATH = getString( R.string.url_base ) + "/payments/user_id/";
		kjh = new KJHttp();
		loadUserBuyedList();
	}

	/**
	 * @Description 初始化用户购买列表
	 * 
	 * @return void
	 */
	private void loadUserBuyedList()
	{
		User user = AppContext.getInstance().getUser();
		if ( user != null )
		{
			userID = user.getId();
			WEB_PATH = WEB_PATH + userID;
			doGetData();
		}
		else
		{
			ViewInject.toast( "网络错误，请重试！" );
			userID = "";
		}
	}

	/**
	 * @Description 初始化用户购买列表
	 * 
	 * @return void
	 */
	private void doGetData()
	{
		Log.e( "xinyTag", WEB_PATH );
		kjh.get( WEB_PATH, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				doLoadJournal( info );
			};

			public void onFailure( int errorNo, String strMsg )
			{
				// Log.e( "xinyiTag", "errorNo:" + errorNo + ", strMsg:" +
				// strMsg );
				ViewInject.toast( "网络错误，请稍后重试" );
			};
		} );
	}

	private void doLoadJournal( String info )
	{
		try
		{
			JSONObject jsonObject = new JSONObject( info );
			JSONArray jsonArray = jsonObject.getJSONArray( "payments" );

			int size = jsonArray.length();
			ArrayList< JournalInfo > dataJournal = new ArrayList< JournalInfo >();

			for ( int i = 0; i < size; i++ )
			{
				JSONObject everyJournal;

				everyJournal = jsonArray.getJSONObject( i );
				if ( everyJournal.getString( "user_id" ).equals( userID ) )
				{
					loadDataForAdapter( dataJournal,
							everyJournal.getString( "journal_id" ) );
				}
			}

			adapter = new HadBuyedAdapter( outsideAty, dataJournal );
			listView.setAdapter( adapter );
		}
		catch ( JSONException e )
		{
			e.printStackTrace();
		}
	}

	private void loadDataForAdapter( final ArrayList< JournalInfo > wan,
			String journalID )
	{
		String web_path = getString( R.string.url_base ) + "/journals/"
				+ journalID;

		kjh.get( web_path, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				try
				{
					JSONObject jsonObject = new JSONObject( info );
					JSONObject jsonObject_journal = jsonObject
							.getJSONObject( "journal" );

					String id = jsonObject_journal.getString( "id" );
					String year = jsonObject_journal.getString( "year" );
					String month = jsonObject_journal.getString( "month" );
					String term = jsonObject_journal.getString( "term" );

					JournalInfo journalInfo = new JournalInfo( year, month,
							term, id );
					wan.add( journalInfo );
					adapter.notifyDataSetChanged();
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag", "errorNo:" + errorNo + ", strMsg:" + strMsg );
			};
		} );
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		setTitle( "已购买" );
	}
};