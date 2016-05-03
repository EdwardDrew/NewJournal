package com.ipbase.fragment;

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
import com.ipbase.adapter.CatalogAdapter;
import com.ipbase.adapter.CatalogAdapter.CatalogItem;
import com.ipbase.bean.FragmentPages;
import com.ipbase.bean.Payment;
import com.ipbase.bean.User;
import com.ipbase.ui.SimpleBackActivity;
import com.ipbase.utils.Installer;
import com.ipbase.utils.NCMUtils;
import com.ipbase.widget.MyDialog;
import com.ipbase.widget.YearMonthWidget;

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

/**
 * ClassName: ArticleListFragment
 * 
 * @Description: 某年某月某期的期刊列表页面
 * @author xinyi
 * @date 2016-02-20
 */
public class ArticleListFragment extends SimpleBackFragment
{
	// 某年的期刊 url
	private String WEB_PATH_Journal;
	// 用户是否购买改年期刊 url
	private String WEB_PATH_IfPay;
	private String WEB_PATH_HADBOUGHT;
	private KJHttp kjh;

	// 用户 ID
	private String mUserId;
	// 期刊 ID
	private String mJournalId;
	// 期刊名称
	private String mJournalName;
	// 自定义列表适配器
	private CatalogAdapter adapter;

	@ BindView( id = R.id.lv_contents )
	private ListView mCatalogLv;

	@ Override
	protected View inflaterView( LayoutInflater inflater, ViewGroup viewGroup,
			Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_contents, null );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		BP.init( outsideAty, AppConfig.Bmob_Key );

		/* 获取数据，数据初始化 */
		Bundle bundle = getArguments();
		if ( bundle != null )
		{
			String temp = bundle.getString( "JournalID" );
			if ( temp != null && !temp.equals( "" ) )
				mJournalId = temp;
			else
				mJournalId = "";
			temp = bundle.getString( "JournalName" );
			if ( temp != null && !temp.equals( "" ) )
			{
				setTitle( temp );
				mJournalName = temp;
			}
			else
			{
				setTitle( "未知期刊" );
				mJournalName = "";
			}
		}
		else
		{
			mJournalId = "";
			mJournalName = "";
			setTitle( "未知期刊" );
		}
		WEB_PATH_Journal = getString( R.string.url_base )
				+ getString( R.string.url_article ) + "/journal_id/"
				+ mJournalId;
		WEB_PATH_IfPay = getString( R.string.url_base ) + "/payments?user_id=";
		WEB_PATH_HADBOUGHT = getString( R.string.url_base ) + "/payments/";
		kjh = new KJHttp();

		User user = AppContext.getInstance().getUser();
		if ( user != null )
		{
			mUserId = user.getId();
		}
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );

		kjh.get( WEB_PATH_Journal, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				super.onSuccess( info );
				getJournalCatalog( info );
			};

			public void onFailure( int errorNo, String strMsg )
			{
			};
		} );
	}

	/**
	 * @Description getJournalCatalog 初始化该年该月该期期刊的文章列表
	 * 
	 * @param info
	 *            服务器后台返回的 json 数据(String)
	 * @return void
	 */
	private void getJournalCatalog( String info )
	{
		JSONObject jsonObject;
		try
		{
			// Log.e( "xinyiTag", info );
			jsonObject = new JSONObject( info );
			JSONArray articleDir_jsonArray = jsonObject
					.getJSONArray( "articles" );
			int size = articleDir_jsonArray.length();
			CatalogItem items[] = new CatalogItem[size];

			for ( int i = 0; i < size; i++ )
			{
				// articleDir为获取到的该月该期的文章目录
				JSONObject articleDir = new JSONObject(
						articleDir_jsonArray.getString( i ) );

				// 某篇文章的其它信息
				String title = articleDir.getString( "title" );
				String author = articleDir.getString( "author" );
				String digest = articleDir.getString( "digest" );
				String article_id = articleDir.getString( "id" );

				items[i] = new CatalogItem( title, author, digest, article_id );
			}

			adapter = new CatalogAdapter( outsideAty, items, mUserId );
			mCatalogLv.setAdapter( adapter );

			initListener();
		}
		catch ( JSONException e )
		{
			e.printStackTrace();
		}
	}

	private void initListener()
	{
		mCatalogLv.setOnItemClickListener( new OnItemClickListener()
		{
			@ Override
			public void onItemClick( AdapterView< ? > parent, View view,
					int pos, long id )
			{
				checkIfUserLoginOrPay( pos );
			}
		} );

		/*if ( YearMonthWidget.DIALOG.isShowing() )
		{
			YearMonthWidget.DIALOG.dismiss();
		}*/
	}

	/**
	 * @Description checkIfUserLoginOrPay 检查该用户是否对该期刊付费
	 * 
	 * @param pos
	 *            用户点击的 item 位置下标(int)
	 * @return void
	 */
	private void checkIfUserLoginOrPay( int pos )
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
				mUserId = user.getId();
				String WEB_PATH = WEB_PATH_IfPay + mUserId + "&journal_id="
						+ mJournalId;

				doCheckIfPay( pos, WEB_PATH );
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
	private void doCheckIfPay( final int pos, String WEB_PATH )
	{
		// Log.e( "xinyiTag", WEB_PATH );
		kjh.get( WEB_PATH, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess( String info )
			{
				try
				{
					Log.e( "xinyiTag", info );
					JSONObject object = new JSONObject( info );
					boolean result = object.getBoolean( "result" );
					if ( result )
					{
						CatalogItem item = (CatalogItem) adapter.getItem( pos );
						Bundle bundle = new Bundle();
						bundle.putString( "ArticleID", item.getArticle_id() );
						bundle.putString( "ArticleTitle", item.getTitle() );
						SimpleBackActivity.postShowWith( outsideAty,
								FragmentPages.WebView_Page, bundle );
					}
					else
					{
						MyDialog myDialog = new MyDialog( outsideAty, "购买 "
								+ mJournalName + " 期刊", "您将花费10￥，是否继续？", false,
								new MyDialog.onClickOKButtonListener()
								{
									@ Override
									public void back( String returnInfo )
									{
										if ( returnInfo != null )
										{
											if ( returnInfo.equals( "OK" ) )
											{
												setPayClickWork();
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

	/**
	 * 设置支付 item 点击事件
	 * 
	 * @param articleID
	 *            文章ID号(String)
	 * 
	 * @return void
	 */
	private void setPayClickWork()
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
				doPostHadBought();
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

			/*
			 * 支付失败,有可能是用户中断支付,也有可能是网络问题
			 * 返回10777时说明上次操作尚未完成,拒绝多次请求以免生成多个订单(可用BP.ForceFree()方法强制取消一次限制)
			 * 支付宝支付时6001为用户主动中断支付操作 微信支付返回-2时为用户主动中断操作,返回-3为未安装Bmob支付插件.apk
			 * (如果多次出现异常请向Bmob工作人员反馈)
			 */
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
		BP.pay( outsideAty, "《新中医》 " + mJournalName + " 期刊", "这本书的表述", 0.01,
				false, payListener );
	}

	// 发送至数据库告知已经购买某期刊
	private void doPostHadBought()
	{
		HttpParams params = new HttpParams();

		Payment payment = new Payment();
		payment.setJournal_id( mJournalId );
		payment.setUser_id( mUserId );

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
};