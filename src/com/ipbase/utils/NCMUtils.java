package com.ipbase.utils;

import android.app.Activity;
import android.view.WindowManager;

import com.ipbase.widget.MyDialog;

public class NCMUtils
{
	@ SuppressWarnings( "deprecation" )
	public static void setDialogFillWidth( Activity aty, MyDialog dialog )
	{
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) ( aty.getWindowManager().getDefaultDisplay()
				.getWidth() ); // 设置宽度
		dialog.getWindow().setAttributes( lp );
	}
};