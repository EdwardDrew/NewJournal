package com.ipbase.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.kymjs.kjframe.ui.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

public class Installer
{
	/**
	 * 从文件中读取插件
	 * 
	 * @param aty
	 *            点击的View的父容器(Activity)
	 * @param fileName
	 *            点击的View(String)
	 * 
	 * @return void
	 */
	public static void loadBmobPayPlugin( Activity aty, String fileName )
	{
		try
		{
			InputStream is = aty.getAssets().open( fileName );
			File file = new File( Environment.getExternalStorageDirectory()
					+ File.separator + fileName + ".apk" );
			if ( file.exists() )
				file.delete();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream( file );
			byte[] temp = new byte[1024];
			int i = 0;
			while ( ( i = is.read( temp ) ) > 0 )
			{
				fos.write( temp, 0, i );
			}
			fos.close();
			is.close();

			Intent intent = new Intent( Intent.ACTION_VIEW );
			intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
			intent.setDataAndType( Uri.parse( "file://" + file ),
					"application/vnd.android.package-archive" );
			aty.startActivity( intent );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			ViewInject.toast( "安装失败...插件文件不存在" );
		}
	}

	public static ArrayList< String > popSort( ArrayList< String > data )
	{
		String temp = null;
		int size = data.size();
		for ( int j = 0; j < size - 1; j++ )
		{
			for ( int i = 0; i < size - 1 - j; i++ )
			{
				if ( Integer.parseInt( data.get( i ) ) < Integer.parseInt( data
						.get( i + 1 ) ) )
				{
					temp = data.get( i );
					data.set( i, data.get( i + 1 ) );
					data.set( i + 1, temp );
				}
			}
		}
		return data;
	}

	public static ArrayList< String > popSortQiKan( ArrayList< String > data )
	{
		String temp = null;
		int size = data.size();
		for ( int j = 0; j < size - 1; j++ )
		{
			for ( int i = 0; i < size - 1 - j; i++ )
			{
				String first = data.get( i ).substring(
						data.get( i ).indexOf( "年" ) + 1,
						data.get( i ).indexOf( "月" ) );
				String second = data.get( i + 1 ).substring(
						data.get( i + 1 ).indexOf( "年" ) + 1,
						data.get( i + 1 ).indexOf( "月" ) );

				if ( Integer.parseInt( first ) > Integer.parseInt( second ) )
				{
					temp = data.get( i );
					data.set( i, data.get( i + 1 ) );
					data.set( i + 1, temp );
				}
			}
		}

		temp = null;
		for ( int j = 0; j < size - 1; j++ )
		{
			for ( int i = 0; i < size - 1 - j; i++ )
			{
				String month1 = data.get( i ).substring(
						data.get( i ).indexOf( "年" ) + 1,
						data.get( i ).indexOf( "月" ) );

				String month2 = data.get( i + 1 ).substring(
						data.get( i + 1 ).indexOf( "年" ) + 1,
						data.get( i + 1 ).indexOf( "月" ) );

				String first = data.get( i ).substring(
						data.get( i ).indexOf( "第" ) + 1,
						data.get( i ).indexOf( "期" ) );
				String second = data.get( i + 1 ).substring(
						data.get( i + 1 ).indexOf( "第" ) + 1,
						data.get( i + 1 ).indexOf( "期" ) );

				if ( Integer.parseInt( month1 ) == Integer.parseInt( month2 )
						&& Integer.parseInt( first ) > Integer
								.parseInt( second ) )
				{
					temp = data.get( i );
					data.set( i, data.get( i + 1 ) );
					data.set( i + 1, temp );
				}
			}
		}

		return data;
	}
};