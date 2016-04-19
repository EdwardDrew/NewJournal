package com.ipbase.widget;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NcmWebViewClient extends WebViewClient
{	
	private ProgressBar mProgressBar;

	public NcmWebViewClient( ProgressBar mProgressBar )
	{
		super();
		this.mProgressBar = mProgressBar;
	}

	@ Override
	public boolean shouldOverrideUrlLoading( WebView view, String url )
	{
		return false;
	}

	@ Override
	public void onPageFinished( WebView view, String url )
	{
		mProgressBar.setVisibility( View.GONE );
		super.onPageFinished( view, url );
	}

	@ Override
	public void onPageStarted( WebView view, String url, Bitmap favicon )
	{
		mProgressBar.setVisibility( View.VISIBLE );
		super.onPageStarted( view, url, favicon );
	}
};