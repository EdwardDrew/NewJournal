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

import c.b.BP;
import c.b.PListener;

import com.alibaba.fastjson.JSON;
import com.ipbase.AppConfig;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.adapter.CollectAdapter;
import com.ipbase.adapter.CollectAdapter.CollectData;
import com.ipbase.bean.FragmentPages;
import com.ipbase.bean.Payment;
import com.ipbase.bean.User;
import com.ipbase.ui.SimpleBackActivity;
import com.ipbase.utils.Installer;
import com.ipbase.utils.NCMUtils;
import com.ipbase.widget.MyDialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CollectionFragment extends SimpleBackFragment
{
	private String WEB_PATH;
	private String WEB_PATH_articleName;
	private String WEB_PATH_IfPay;
	private String WEB_PATH_HADBOUGHT;
	private KJHttp kjh;

	private String userID;
	@ BindView( id = R.id.lv_collected )
	private ListView listView;
	private CollectAdapter adapter;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_collected, null );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		BP.init( outsideAty, AppConfig.Bmob_Key );
		setTitle( "已收藏" );

		WEB_PATH = getString( R.string.url_base ) + "/collects/user_id/";
		WEB_PATH_articleName = outsideAty.getString( R.string.url_base )
				+ "/articles/";
		WEB_PATH_IfPay = getString( R.string.url_base ) + "/payments?user_id=";
		WEB_PATH_HADBOUGHT = getString( R.string.url_base ) + "/payments/";
		kjh = new KJHttp();
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		initCollectionList();
		setItemClick();
	}

	private void initCollectionList()
	{
		User user = null;
		user = AppContext.getInstance().getUser();
		if ( user != null )
		{
			userID = user.getId();
		}
		WEB_PATH = WEB_PATH + userID;

		kjh.jsonGet( WEB_PATH, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				// Log.e( "xinyiTag 收藏文章的请求", info );
				try
				{
					ArrayList< CollectData > data = new ArrayList< CollectData >();

					JSONObject jsonObject = new JSONObject( info );
					JSONArray jsonArray = jsonObject.getJSONArray( "collects" );

					int size = jsonArray.length();

					for ( int i = 0; i < size; i++ )
					{
						JSONObject collects = jsonArray.getJSONObject( i );
						String articleID = collects.getString( "article_id" );
						String collectID = collects.getString( "id" );

						CollectData collectData = new CollectData();
						collectData.setArticleID( articleID );
						collectData.setCollectID( collectID );
						doGetAticleName( articleID, data, collectData );
					}

					adapter = new CollectAdapter( outsideAty, data );
					listView.setAdapter( adapter );
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

	private void doGetAticleName( String aticleID,
			final ArrayList< CollectData > data, final CollectData collectData )
	{
		String urlPath = WEB_PATH_articleName + aticleID;

		kjh.jsonGet( urlPath, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				try
				{
					JSONObject jsonObject = new JSONObject( info );
					JSONObject articleObj = jsonObject
							.getJSONObject( "article" );

					String articleName = articleObj.getString( "title" );
					String journalName = articleObj.getString( "journal_name" );
					String journalId = articleObj.getString( "journal_id" );

					collectData.setArticleName( articleName );
					collectData.setJournalID( journalId );
					collectData.setJournalName( journalName );

					data.add( collectData );
					adapter.notifyDataSetChanged();
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 加载收藏列表文章名", "错误码：" + errorNo + " ,错误信息："
						+ strMsg );
			};

			public void onFinish()
			{
			};
		} );
	}

	private void setItemClick()
	{
		listView.setOnItemClickListener( new OnItemClickListener()
		{
			@ Override
			public void onItemClick( AdapterView< ? > parent, View view,
					int pos, long id )
			{
				CollectData collectData = (CollectData) adapter.getItem( pos );
				checkIfUserLoginOrPay( pos, collectData );
			}
		} );
	}

	/**
	 * @Description checkIfUserLoginOrPay 检查该用户是否对该期刊付费
	 * 
	 * @param pos
	 *            用户点击的 item 位置下标(int)
	 * @return void
	 */
	private void checkIfUserLoginOrPay( int pos, CollectData collectData )
	{
		User user = AppContext.getInstance().getUser();
		if ( user != null )
		{
			// 如果检测不到登录用户
			if ( user.getId() == null || user.getId().equals( "" ) )
			{
				ViewInject.toast( "未知错误，请重新登录再试~" );
				return;
			}
			else
			{
				doCheckIfPay( pos, user.getId(), collectData );
			}
		}
		else
		{
			ViewInject.toast( "请先登录！" );
		}
	}

	/**
	 * @Description doCheckIfPay 访问指定文章的 Html 页面
	 * 
	 * @param pos
	 *            用户点击的 item 位置下标(final int)
	 * @param WEB_PATH
	 *            访问改篇文章的 url 地址(String)
	 * @return void
	 */
	private void doCheckIfPay( final int pos, final String userID,
			final CollectData collectData )
	{
		String checkPay = WEB_PATH_IfPay + userID + "&journal_id="
				+ collectData.getJournalID();

		kjh.get( checkPay, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				try
				{
					JSONObject object = new JSONObject( info );
					boolean result = object.getBoolean( "result" );
					if ( result )
					{
						CollectData item = (CollectData) adapter.getItem( pos );
						Bundle bundle = new Bundle();
						bundle.putString( "ArticleID", item.getArticleID() );
						bundle.putString( "ArticleTitle", item.getArticleName() );
						SimpleBackActivity.postShowWith( outsideAty,
								FragmentPages.WebView_Page, bundle );
					}
					else
					{
						doBuyWork( collectData.getJournalName(), userID,
								collectData.getJournalID() );
					}
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			}

			public void onFailure( int errorNo, String strMsg )
			{
				ViewInject.toast( "未知错误，请重新登录再试~" );
			}
		} );
	}

	private void doBuyWork( final String journalName, final String userID,
			final String journalID )
	{
		MyDialog myDialog = new MyDialog( outsideAty, "购买 " + journalName
				+ " 期刊", "您将花费10￥，是否继续？", false,
				new MyDialog.onClickOKButtonListener()
				{
					@ Override
					public void back( String returnInfo )
					{
						if ( returnInfo != null )
						{
							if ( returnInfo.equals( "OK" ) )
							{
								setPayClickWork( journalName, userID, journalID );
							}
							else
							{
								ViewInject.toast( "您取消了购买！" );
							}
						}
					}
				}, MyDialog.OKCancel );
		myDialog.show();
		NCMUtils.setDialogFillWidth( outsideAty, myDialog );
	}

	/**
	 * 设置支付 item 点击事件
	 * 
	 * @param articleID
	 *            文章ID号(String)
	 * 
	 * @return void
	 */
	private void setPayClickWork( String journalName, final String userID,
			final String journalID )
	{
		final ProgressDialog dialog = ViewInject.getprogress( outsideAty,
				"正在通讯，请稍候...", false );

		PListener payListener = new PListener()
		{
			// 因为网络等问题,不能确认是否支付成功,请稍后手动查询(小概率事件)
			@ Override
			public void unknow()
			{
				ViewInject.toast( "支付结果未知,请稍后手动查询" );
				dialog.dismiss();
			}

			// 支付成功,保险起见请调用查询方法确认结果
			@ Override
			public void succeed()
			{
				doPostHadBought( userID, journalID );
				ViewInject.toast( "支付成功 !" );
				dialog.dismiss();
			}

			// 无论支付成功与否,只要成功产生了请求,就返回订单号,请自行保存以便以后查询
			@ Override
			public void orderId( String orderId )
			{
				// ViewInject.toast( "获取订单成功!请等待跳转到支付页面~" );
				Log.e( "xinyiTag", "获取订单成功!请等待跳转到支付页面~" );
			}

			@ Override
			public void fail( int code, String reason )
			{
				if ( code == -3 )
				{
					installWeiXinPay();
				}
				else
				{
					ViewInject.toast( "支付取消 !" );
				}
				dialog.dismiss();
			}
		};

		// Bmob支付
		BP.pay( outsideAty, "《新中医》 " + journalName + " 期刊", "这本书的表述", 0.01,
				false, payListener );
	}

	// 发送至数据库告知已经购买某期刊
	private void doPostHadBought( String userID, String journalID )
	{
		HttpParams params = new HttpParams();

		Payment payment = new Payment();
		payment.setJournal_id( journalID );
		payment.setUser_id( userID );

		params.putJsonParams( JSON.toJSONString( payment ) );

		kjh.jsonPost( WEB_PATH_HADBOUGHT, params, false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				try
				{
					JSONObject jsonObject = new JSONObject( info );
					boolean result = jsonObject.getBoolean( "result" );
					if ( result )
					{
						ViewInject.toast( "购买成功!" );
					}
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}
			};

			public void onFailure( int errorNo, String strMsg )
			{
				Log.e( "xinyiTag 期刊付款错误", "错误码：" + errorNo + ", 错误信息：" + strMsg );
			};

			public void onFinish()
			{
			};
		} );
	}

	/**
	 * 安装微信支付插件
	 * 
	 * @return void
	 */
	private void installWeiXinPay()
	{
		new AlertDialog.Builder( outsideAty )
				.setMessage( "监测到你尚未安装微信支付插件,是否现在安装？(无需耗费流量)" )
				.setPositiveButton( "安装", new DialogInterface.OnClickListener()
				{
					@ Override
					public void onClick( DialogInterface dialog, int which )
					{
						Installer.loadBmobPayPlugin( outsideAty,
								"bmob_pay_wx.db" );
					}
				} )
				.setNegativeButton( "取消", new DialogInterface.OnClickListener()
				{
					@ Override
					public void onClick( DialogInterface dialog, int which )
					{
						dialog.dismiss();
					}
				} ).create().show();
	}

	@ Override
	protected void widgetClick( View v )
	{
		super.widgetClick( v );
	}
};