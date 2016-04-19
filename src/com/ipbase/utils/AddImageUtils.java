package com.ipbase.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import org.kymjs.kjframe.ui.KJFragment;

import com.ipbase.AppConfig;
import com.ipbase.widget.ActionSheetDialog;

import java.io.File;
import java.io.IOException;

/**
 * 获取图片工具类
 */
public class AddImageUtils
{
	public static final int REQUEST_CODE_CAIJIANZHAOPIAN = 0x111;// 裁剪照片
	public static final int REQUEST_CODE_ALBUM = 0x222;// 从相册获取

	private static KJFragment mFragment;
	private static Activity mActivity;

	private static File file;

	/**
	 * 启动
	 *
	 * @param aty
	 */
	public static void start( Activity aty, KJFragment pFragment )
	{
		mActivity = aty;
		mFragment = pFragment;
		file = null;
		showSelectPhotoSheet();
	}

	/**
	 * 显示拍照，相册列表
	 */
	private static void showSelectPhotoSheet()
	{
		new ActionSheetDialog( mActivity )
				.builder()
				.setTitle( "请选择操作" )
				.setCancelable( false )
				.setCanceledOnTouchOutside( false )
				.addSheetItem( "拍照", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener()
						{
							@ Override
							public void onClick( int which )
							{
								takePhoto();
							}
						} )
				.addSheetItem( "从相册获取", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener()
						{
							@ Override
							public void onClick( int which )
							{
								fromAlbum();
							}
						} ).show();
	}

	// 从相册中获取图片
	private static void fromAlbum()
	{
		Intent intent;
		if ( Build.VERSION.SDK_INT < 19 )
		{
			intent = new Intent();
			intent.setAction( Intent.ACTION_GET_CONTENT );
			intent.setType( "image/*" );
			mFragment.startActivityForResult(
					Intent.createChooser( intent, "选择图片" ), REQUEST_CODE_ALBUM );
		}
		else
		{
			intent = new Intent( Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
			intent.setType( "image/*" );
			mFragment.startActivityForResult(
					Intent.createChooser( intent, "选择图片" ), REQUEST_CODE_ALBUM );
		}
	}

	/**
	 * 自行拍照
	 */
	private static void takePhoto()
	{
		Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
		intent.putExtra( MediaStore.EXTRA_OUTPUT,
				Uri.fromFile( creatImagePath() ) );
		mFragment.startActivityForResult( intent, REQUEST_CODE_CAIJIANZHAOPIAN );
	}

	/**
	 * 创建保存相册的路径
	 *
	 * @return
	 */
	private static File creatImagePath()
	{
		String state = Environment.getExternalStorageState();
		if ( state.equals( Environment.MEDIA_MOUNTED ) )
		{
			String saveDir = Environment.getExternalStorageDirectory()
					+ AppConfig.imgCachePath;
			File dir = new File( saveDir );
			if ( !dir.exists() )
			{
				dir.mkdir();
			}
			file = new File( saveDir, "image.png" );
			file.delete();
			if ( !file.exists() )
			{
				try
				{
					file.createNewFile();
				}
				catch ( IOException e )
				{
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 得到文件类
	 *
	 * @return
	 */
	public static File getFile()
	{
		return file;
	}
};