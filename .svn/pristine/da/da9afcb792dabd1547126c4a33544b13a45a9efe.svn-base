package com.ipbase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NCMScrollView extends ScrollView
{
	private GestureDetector mGestureDetector;

	public NCMScrollView( Context context )
	{
		this( context, null );
	}

	public NCMScrollView( Context context, AttributeSet attrs )
	{
		this( context, attrs, 0 );
	}

	public NCMScrollView( Context context, AttributeSet attrs, int defStyleAttr )
	{
		super( context, attrs, defStyleAttr );
		mGestureDetector = new GestureDetector( context, new YScrollDetector() );
		setFadingEdgeLength( 0 );
	}

	@ Override
	public boolean onInterceptTouchEvent( MotionEvent ev )
	{
		return super.onInterceptTouchEvent( ev )
				&& mGestureDetector.onTouchEvent( ev );
	}

	// Return false if we're scrolling in the x direction
	private class YScrollDetector extends SimpleOnGestureListener
	{
		@ Override
		public boolean onScroll( MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY )
		{
			if ( Math.abs( distanceY ) > Math.abs( distanceX ) )
			{
				return true;
			}
			return false;
		}
	}
};