package com.ipbase.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebSettings.RenderPriority;

public class NcmWebView extends WebView
{
	private OnScrollChangeListener listener;

	public NcmWebView( Context context )
	{
		this( context, null );
	}

	public NcmWebView( Context context, AttributeSet attrs )
	{
		this( context, attrs, 0 );
	}

	public NcmWebView( Context context, AttributeSet attrs, int defStyleAttr )
	{
		this( context, attrs, defStyleAttr, false );
	}

	@ SuppressLint( "SetJavaScriptEnabled" )
	@ SuppressWarnings( "deprecation" )
	public NcmWebView( Context context, AttributeSet attrs, int defStyleAttr,
			boolean privateBrowsing )
	{
		super( context, attrs, defStyleAttr, privateBrowsing );
		getSettings().setJavaScriptEnabled( true );
		getSettings().setBuiltInZoomControls( true );
		getSettings().setDisplayZoomControls( false );
		getSettings().setUseWideViewPort( true );
		getSettings().setRenderPriority( RenderPriority.HIGH );
	}

	@ SuppressWarnings( "deprecation" )
	@ Override
	protected void onScrollChanged( int l, int t, int oldl, int oldt )
	{
		super.onScrollChanged( l, t, oldl, oldt );

		float webcontent = getContentHeight() * getScale();// webview的高度
		float webnow = getHeight() + getScrollY();// 当前webview的高度

		if ( Math.abs( webcontent - webnow ) < 1 )
		{
			// 底端
			listener.onPageEnd( l, t, oldl, oldt );
		}
		else if ( getScrollY() == 0 )
		{
			// 顶端
			listener.onPageTop( l, t, oldl, oldt );
		}
		else
		{
			// 滚动改变
			listener.onScrollChanged( l, t, oldl, oldt );
		}
	}

	public void setOnScrollChangeListener( OnScrollChangeListener listener )
	{
		this.listener = listener;
	}

	public interface OnScrollChangeListener
	{
		public void onPageEnd( int l, int t, int oldl, int oldt );

		public void onPageTop( int l, int t, int oldl, int oldt );

		public void onScrollChanged( int l, int t, int oldl, int oldt );
	}

};