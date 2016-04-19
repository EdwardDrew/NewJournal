package com.ipbase.ui;

import java.io.File;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

import com.ipbase.AppApplication;
import com.ipbase.AppConfig;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.bean.FragmentPages;
import com.ipbase.bean.MyBmobInstallation;
import com.ipbase.broadcastreceiver.NetReceiver;
import com.ipbase.residemenu.SlidingMenu;
import com.ipbase.widget.YearMonthWidget;

public class MainActivity extends KJActivity
{
	// 自定义侧滑菜单控件
	private SlidingMenu mSlidingMenu;
	// 点击 Back 键间隔时间
	private long mExitTime;
	// 搜索按钮
	@ BindView( id = R.id.title_bar_right_menu, click = true )
	private Button mSearchButton;
	// 年月期刊控件
	private YearMonthWidget mYearMonthWidget;
	private NetReceiver myReceiver;

	// 更新返回
	private UpdateResponse ur;

	private Activity aty;

	@ Override
	public void setRootView()
	{
		setContentView( R.layout.activity_main );
		aty = this;
	}

	@ Override
	public void initData()
	{
		super.initData();
		AppApplication.getInstance().setMainActivity( this );

		// 初始化BmobSDK
		Bmob.initialize( this, AppConfig.Bmob_Key );
		// 使用推送服务时的初始化操作
		BmobInstallation.getCurrentInstallation( this ).save();
		if ( AppContext.getInstance().getUser() != null )
		{
			// 更新MyBmobInstallation里的uid
			updateMyBmobInstallationUid();
		}
		// 启动推送服务
		BmobPush.startWork( this, AppConfig.Bmob_Key );

		// 在你需要调用自动更新功能之前先进行初始化建表操作
		// 此方法适合开发者调试自动更新功能时使用，一旦AppVersion表在后台创建成功，建议屏蔽或删除此方法，否则会造成生成多行记录。
		// BmobUpdateAgent.initAppVersion(this);

		// 允许在非wifi环境下检测应用更新
		BmobUpdateAgent.setUpdateOnlyWifi( false );
		// 更新监听器
		BmobUpdateAgent.setUpdateListener( new BmobUpdateListener()
		{
			@ Override
			public void onUpdateReturned( int updateStatus,
					UpdateResponse updateInfo )
			{
				// 以下适用于V3.4.4之前版本
				if ( updateStatus == UpdateStatus.Yes )
				{// 版本有更新
					ur = updateInfo;
					// Log.i("smile", "应用的target_size的大小 = "+new
					// File("sdcard/NewJournal.apk").length());
				}
			}
		} );
		// DeleteExitingApk();
	}

	/**
	 * @Title: updateMyBmobInstallationUid
	 * @Description: 增加Installation表中的一个字段uid
	 * @return void
	 * @throws
	 * @auhor xianhua
	 * @date 2016年3月16日
	 */
	private void updateMyBmobInstallationUid()
	{
		BmobQuery< MyBmobInstallation > query = new BmobQuery< MyBmobInstallation >();
		query.addWhereEqualTo( "installationId",
				BmobInstallation.getInstallationId( this ) );
		query.findObjects( this, new FindListener< MyBmobInstallation >()
		{

			@ Override
			public void onSuccess( List< MyBmobInstallation > object )
			{
				if ( object.size() > 0 )
				{
					MyBmobInstallation mbi = object.get( 0 );
					mbi.setUid( AppContext.getInstance().getUser().getPhone() );
					mbi.update( aty, new UpdateListener()
					{

						@ Override
						public void onSuccess()
						{
							// Log.i("bmob", "设备信息更新成功");
						}

						@ Override
						public void onFailure( int code, String msg )
						{
							Log.i( "bmob", "设备信息更新失败:" + msg );
						}
					} );
				}
				else
				{
				}
			}

			@ Override
			public void onError( int code, String msg )
			{
				ViewInject.toast( "错误码：" + code + "，错误信息:" + msg );
			}
		} );
	}

	/**
	 * @Title: DeleteExitingApk
	 * @Description: 删除SD卡根目录下的安装包
	 * @param
	 * @return void
	 * @throws
	 * @auhor 鲜花
	 * @date 2015年11月6日
	 */
	@ SuppressWarnings( "unused" )
	private void DeleteExitingApk()
	{
		if ( ur != null )
		{
			File file = new File( Environment.getExternalStorageDirectory(),
					ur.path_md5 + ".apk" );
			Log.i( "ur.path=", ur.path );
			Log.i( "ur.path_md5=", ur.path_md5 );
			Log.i( "应用的target_size的大小 = ", file.length() + "" );
			if ( file != null && file.exists() )
			{
				if ( file.delete() )
				{
					Log.i( "DeleteExitingApk1", "删除完成" );
				}
				else
				{
					Log.i( "DeleteExitingApk1", "删除失败" );
				}
			}
			else
			{
				Log.i( "DeleteExitingApk2", "删除完成" );
			}
		}
		else
		{
			Log.i( "DeleteExitingApk3", "删除失败" );
		}
	}

	@ Override
	public void initWidget()
	{
		super.initWidget();

		mSlidingMenu = new SlidingMenu( this,
				(Button) findViewById( R.id.title_bar_left_menu ) );
		if ( AppContext.getInstance().getUser() != null )
		{
			ViewInject.toast( AppContext.getInstance().getUser().getPhone()
					+ "登录成功" );
		}
		mYearMonthWidget = new YearMonthWidget( this );
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION );
		myReceiver = new NetReceiver( this, mYearMonthWidget );
		this.registerReceiver( myReceiver, filter );
		// 自动更新
		BmobUpdateAgent.update( this );
	}

	@ Override
	public void widgetClick( View view )
	{
		switch ( view.getId() )
		{
			case R.id.title_bar_right_menu :
				SimpleBackActivity.postShowWith( this,
						FragmentPages.Search_Page );
				break;

			default :
				break;
		}
		super.widgetClick( view );
	}

	@ Override
	public boolean dispatchTouchEvent( MotionEvent ev )
	{
		return mSlidingMenu.getResideMenu().dispatchTouchEvent( ev );
	}

	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		if ( keyCode == KeyEvent.KEYCODE_BACK )
		{
			// 判断菜单是否关闭
			if ( mSlidingMenu.getIsClosed() )
			{
				// 判断两次点击的时间间隔（默认设置为2秒）
				if ( ( System.currentTimeMillis() - mExitTime ) > 2000 )
				{
					ViewInject.toast( "再按一次退出程序" );
					mExitTime = System.currentTimeMillis();
				}
				else
				{
					finish();
					System.exit( 0 );
					super.onBackPressed();
				}
			}
			else
			{
				mSlidingMenu.getResideMenu().closeMenu();
			}
			return true;
		}
		return super.onKeyDown( keyCode, event );
	}

	/**
	 * 
	 * @Description: 刷新主界面
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015年9月26日
	 */
	public void reFresh()
	{
		// 刷新菜单
		mSlidingMenu.reFresh();
	}

	@ Override
	protected void onDestroy()
	{
		if ( myReceiver != null )
		{
			this.unregisterReceiver( myReceiver );
		}
		super.onDestroy();
	}

}
