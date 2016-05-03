package com.ipbase.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.adapter.CatalogAdapter;
import com.ipbase.adapter.SearchResultAdapter;
import com.ipbase.adapter.CatalogAdapter.CatalogItem;
import com.ipbase.bean.Article;
import com.ipbase.bean.FragmentPages;
import com.ipbase.bean.Payment;
import com.ipbase.bean.User;
import com.ipbase.ui.SimpleBackActivity;
import com.ipbase.utils.CommonUtils;
import com.ipbase.utils.FastJsonTools;
import com.ipbase.utils.Installer;
import com.ipbase.utils.NCMUtils;
import com.ipbase.widget.MyDialog;
import com.ipbase.widget.YearMonthWidget;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 搜索结果页面
 * 
 * @author 李先华 2015年11月5日下午10:45:48
 */
public class SearchResultFragment extends SimpleBackFragment implements
		TextWatcher
{
	private String WEB_PATH;

	private KJHttp kjh;

	// 这里不能用KJFrame绑定控件id
	private EditText mEitSearch;
	private ImageView mIvSearch;
	private ImageView mIvDeleteText;

	@BindView(id = R.id.lv_search_result)
	private ListView mLvSearchResult;

	private ArrayList<Article> mArticles = null;

	// private SearchResultAdapter mSearchResultAdapter;

	private Article mArticle;

	// 搜索词
	private String mSearchWord = null;

	// 自定义列表适配器
	private CatalogAdapter adapter;

	// 用户 ID
	private String mUserId;

	// 期刊 ID
	private String mJournalId = "";
	
	// 期刊名称
	private String mJournalName = "";

	// 用户是否购买改年期刊 url
	private String WEB_PATH_IfPay;
	private String WEB_PATH_HADBOUGHT;

	private CatalogItem mItems[];

	@Override
	protected View inflaterView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2)
	{
		return View.inflate(outsideAty, R.layout.fragment_search_result, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initData()
	{
		// TODO Auto-generated method stub
		super.initData();
		// 获得传过来的List<Object>
		// articles = getArguments().getParcelableArrayList("mArticles");
		mArticles = (ArrayList<Article>) getBundleData().getSerializable(
				"mArticles");
		mSearchWord = getArguments().getString("SearchWord");

		WEB_PATH = getString(R.string.url_base)
				+ getString(R.string.url_search);

		WEB_PATH_IfPay = getString(R.string.url_base) + "/payments?user_id=";
		WEB_PATH_HADBOUGHT = getString(R.string.url_base) + "/payments/";

		kjh = new KJHttp();

		User user = AppContext.getInstance().getUser();
		if (user != null)
		{
			mUserId = user.getId();
		}
	}

	@Override
	protected void initWidget(View parentView)
	{
		super.initWidget(parentView);
		setRLVisible(View.GONE);
		setRLSearchVisible(View.VISIBLE);

		mItems = new CatalogItem[mArticles.size()];
		for (int i = 0; i < mArticles.size(); i++)
		{
			// 某篇文章的其它信息
			String title = mArticles.get(i).getTitle();
			String author = mArticles.get(i).getAuthor();
			String digest = mArticles.get(i).getDigest();
			String article_id = mArticles.get(i).getId();
			String journal_id = mArticles.get(i).getJournal_id();
			mItems[i] = new CatalogItem(title, author, digest, article_id,
					journal_id);
		}
		adapter = new CatalogAdapter(outsideAty, mItems, mUserId);
		mLvSearchResult.setAdapter(adapter);
		initListener();

		findViewById();
		// 初始化EditText的监听器
		initTextChangedListener();

		mEitSearch.setText(mSearchWord);

		RefreshSearch();

	}

	private void initListener()
	{
		mLvSearchResult.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id)
			{
				CatalogItem item = (CatalogItem) adapter.getItem(pos);
				mJournalId = item.getJournal_id();
				getJournalNameByJournalId();
				checkIfUserLoginOrPay(pos);
			}
		});
	}

	protected void getJournalNameByJournalId()
	{
		
	}

	/**
	 * @Description checkIfUserLoginOrPay 检查该用户是否对该期刊付费
	 * 
	 * @param pos
	 *            用户点击的 item 位置下标(int)
	 * @return void
	 */
	private void checkIfUserLoginOrPay(int pos)
	{
		User user = AppContext.getInstance().getUser();
		if (user != null)
		{
			// 如果检测不到登录用户
			if (user.getId() == null || user.getId().equals(""))
			{
				ViewInject.toast("未知错误，请重新登录再试~");
				return;
			}
			else
			{
				mUserId = user.getId();
				String WEB_PATH = WEB_PATH_IfPay + mUserId + "&journal_id="
						+ mJournalId;

				doCheckIfPay(pos, WEB_PATH);
			}
		}
		else
		{
			ViewInject.toast("请先登录！");
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
	private void doCheckIfPay(final int pos, String WEB_PATH)
	{
		// Log.e( "xinyiTag", WEB_PATH );
		kjh.get(WEB_PATH, new HttpParams(), false, new HttpCallBack()
		{
			public void onSuccess(String info)
			{
				try
				{
					Log.e("xinyiTag", info);
					JSONObject object = new JSONObject(info);
					boolean result = object.getBoolean("result");
					if (result)
					{
						CatalogItem item = (CatalogItem) adapter.getItem(pos);
						Bundle bundle = new Bundle();
						bundle.putString("ArticleID", item.getArticle_id());
						bundle.putString("ArticleTitle", item.getTitle());
						SimpleBackActivity.postShowWith(outsideAty,
								FragmentPages.WebView_Page, bundle);
					}
					else
					{
						MyDialog myDialog = new MyDialog(outsideAty, "购买 "
								+ mJournalName + " 期刊", "您将花费10￥，是否继续？", false,
								new MyDialog.onClickOKButtonListener()
								{
									@Override
									public void back(String returnInfo)
									{
										if (returnInfo != null)
										{
											if (returnInfo.equals("OK"))
											{
												setPayClickWork();
											}
											else
											{
												ViewInject.toast("您取消了购买！");
											}
										}

									}
								}, MyDialog.OKCancel);
						myDialog.show();
						NCMUtils.setDialogFillWidth(outsideAty, myDialog);
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			public void onFailure(int errorNo, String strMsg)
			{
				ViewInject.toast("未知错误，请重新登录再试~");
			}
		});
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
		final ProgressDialog dialog = ViewInject.getprogress(outsideAty,
				"正在通讯，请稍候...", false);

		PListener payListener = new PListener()
		{
			// 因为网络等问题,不能确认是否支付成功,请稍后手动查询(小概率事件)
			@Override
			public void unknow()
			{
				ViewInject.toast("支付结果未知,请稍后手动查询");
				dialog.dismiss();
			}

			// 支付成功,保险起见请调用查询方法确认结果
			@Override
			public void succeed()
			{
				doPostHadBought();
				ViewInject.toast("支付成功 !");
				dialog.dismiss();
			}

			// 无论支付成功与否,只要成功产生了请求,就返回订单号,请自行保存以便以后查询
			@Override
			public void orderId(String orderId)
			{
				// ViewInject.toast( "获取订单成功!请等待跳转到支付页面~" );
				Log.e("xinyiTag", "获取订单成功!请等待跳转到支付页面~");
			}

			/*
			 * 支付失败,有可能是用户中断支付,也有可能是网络问题
			 * 返回10777时说明上次操作尚未完成,拒绝多次请求以免生成多个订单(可用BP.ForceFree()方法强制取消一次限制)
			 * 支付宝支付时6001为用户主动中断支付操作 微信支付返回-2时为用户主动中断操作,返回-3为未安装Bmob支付插件.apk
			 * (如果多次出现异常请向Bmob工作人员反馈)
			 */
			@Override
			public void fail(int code, String reason)
			{
				if (code == -3)
				{
					installWeiXinPay();
				}
				else
				{
					ViewInject.toast("支付取消 !");
				}
				dialog.dismiss();
			}
		};

		// Bmob支付
		BP.pay(outsideAty, "《新中医》 " + mJournalName + " 期刊", "这本书的表述", 0.01,
				false, payListener);
	}

	// 发送至数据库告知已经购买某期刊
	private void doPostHadBought()
	{
		HttpParams params = new HttpParams();

		Payment payment = new Payment();
		payment.setJournal_id(mJournalId);
		payment.setUser_id(mUserId);

		params.putJsonParams(JSON.toJSONString(payment));

		kjh.jsonPost(WEB_PATH_HADBOUGHT, params, false, new HttpCallBack()
		{
			public void onSuccess(String info)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(info);
					boolean result = jsonObject.getBoolean("result");
					if (result)
					{
						ViewInject.toast("购买成功!");
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			};

			public void onFailure(int errorNo, String strMsg)
			{
				Log.e("xinyiTag 期刊付款错误", "错误码：" + errorNo + ", 错误信息：" + strMsg);
			};

			public void onFinish()
			{
			};
		});
	}

	/**
	 * 安装微信支付插件
	 * 
	 * @return void
	 */
	private void installWeiXinPay()
	{
		new AlertDialog.Builder(outsideAty)
				.setMessage("监测到你尚未安装微信支付插件,是否现在安装？(无需耗费流量)")
				.setPositiveButton("安装", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Installer.loadBmobPayPlugin(outsideAty,
								"bmob_pay_wx.db");
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).create().show();
	}

	@Override
	protected void widgetClick(View v)
	{
		super.widgetClick(v);
		switch (v.getId())
		{
		case R.id.iv_search:
			RefreshSearch();
			break;
		case R.id.iv_delete_text:
			mEitSearch.setText("");
			break;
		default:
			break;
		}
	}

	private void RefreshSearch()
	{
		if (!CommonUtils.hasNetwork(getActivity()))
		{
			ViewInject.toast("请检查网络连接!");
			return;
		}

		if (mEitSearch.getText().toString().trim().isEmpty())
		{
			ViewInject.toast("请输入搜索内容(作者或者文章标题)");
			return;
		}
		// 搜索词
		String searchWord = null;
		try
		{
			searchWord = URLEncoder.encode(mEitSearch.getText().toString()
					.trim(), "UTF-8"); // 中文传输解决
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		// 搜索
		getSearchResultFromServer(WEB_PATH + searchWord, kjh, this);
	}

	private void findViewById()
	{

		mIvDeleteText = (ImageView) outsideAty
				.findViewById(R.id.iv_delete_text);

		mEitSearch = (EditText) outsideAty.findViewById(R.id.et_search);

		mIvSearch = (ImageView) outsideAty.findViewById(R.id.iv_search);

		mIvDeleteText.setOnClickListener(this);

		mIvSearch.setOnClickListener(this);
	}

	private void initTextChangedListener()
	{
		mEitSearch.addTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{

	}

	@Override
	public void afterTextChanged(Editable s)
	{
		if (s.length() == 0)
		{
			mIvDeleteText.setVisibility(View.GONE);
		}
		else
		{
			mIvDeleteText.setVisibility(View.VISIBLE);
		}
	}

	private void getSearchResultFromServer(String WEB_PATH, KJHttp kjh,
			final SimpleBackFragment fragment)
	{

		kjh.get(WEB_PATH, new HttpParams(), false, new HttpCallBack()
		{
			@Override
			public void onSuccess(String t)
			{
				super.onSuccess(t);

				// Log.i("SearchResultFragment onSuccess ", t);
				try
				{
					ArrayList<Article> articleLists = null;
					JSONObject object = new JSONObject(t);
					boolean success = object.getBoolean("result");
					String articles = object.getString("articles");
					// Log.i("SearchResultFragment articlesTitle",
					// articlesTitle);
					// Log.i("SearchResultFragment articlesAuthor",
					// articlesAuthor);
					if (success)
					{
						articleLists = (ArrayList<Article>) FastJsonTools
								.getBeans(articles, Article.class);
						// 更改listview数据
						/*
						 * mSearchResultAdapter = new SearchResultAdapter(
						 * outsideAty, articleLists );
						 * mLvSearchResult.setAdapter( mSearchResultAdapter );
						 */
						mItems = new CatalogItem[mArticles.size()];
						for (int i = 0; i < mArticles.size(); i++)
						{
							// 某篇文章的其它信息
							String title = mArticles.get(i).getTitle();
							String author = mArticles.get(i).getAuthor();
							String digest = mArticles.get(i).getDigest();
							String article_id = mArticles.get(i).getId();
							String journal_id = mArticles.get(i).getJournal_id();
							mItems[i] = new CatalogItem(title, author, digest, article_id,
									journal_id);
						}
						adapter = new CatalogAdapter(outsideAty, mItems, mUserId);
						mLvSearchResult.setAdapter(adapter);
						initListener();
					}
					else
					{
						ViewInject.toast(object.getString("msg"));
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					ViewInject.toast("JSONException " + e.getMessage());
				}

			}

			@Override
			public void onFailure(int errorNo, String strMsg)
			{
				super.onFailure(errorNo, strMsg);
				ViewInject.toast("错误码:" + errorNo + ", 错误信息:" + strMsg);
			}

		});

	}
};