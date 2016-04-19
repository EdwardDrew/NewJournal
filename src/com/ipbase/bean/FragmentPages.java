package com.ipbase.bean;

import com.ipbase.fragment.ArticleListFragment;
import com.ipbase.fragment.ChangePasswordFragment;
import com.ipbase.fragment.CollectionFragment;
import com.ipbase.fragment.FeedbackFragment;
import com.ipbase.fragment.FindPasswordFragment;
import com.ipbase.fragment.HadBuyedFragment;
import com.ipbase.fragment.LoginFragment;
import com.ipbase.fragment.LookCommentsFragment;
import com.ipbase.fragment.NoticeFragment;
import com.ipbase.fragment.PersonFragment;
import com.ipbase.fragment.ScanArticleFragment;
import com.ipbase.fragment.SearchFragment;
import com.ipbase.fragment.SearchResultFragment;
import com.ipbase.fragment.SettingFragment;
import com.ipbase.fragment.TouGaoFragment;
import com.ipbase.fragment.WriteCommentsFragment;
import com.ipbase.fragment.YiTouGaoFragment;

/**
 * 
 * ClassName: SimpleBackPage
 * 
 * @Description: 返回页的基本信息注册 (其实就是将原本会在Manifest中注册的Activity转成Fragment在这里注册)
 * 
 * @author yokoboy
 * @date 2015-9-1
 */
public enum FragmentPages
{
	// 注册页面
	Login_Page( 1, LoginFragment.class ), ChangePassword_Page( 2,
			ChangePasswordFragment.class ), Search_Page( 3,
			SearchFragment.class ), SearchResult_Page( 4,
			SearchResultFragment.class ), Setting_Page( 5,
			SettingFragment.class ), TouGao_Page( 6, TouGaoFragment.class ), FindPwd(
			7, FindPasswordFragment.class ), PersonInfo( 8,
			PersonFragment.class ), Feedback_Page( 9, FeedbackFragment.class ), YiTouGao_page(
			10, YiTouGaoFragment.class ), ArticleList_Page( 11,
			ArticleListFragment.class ), WebView_Page( 12,
			ScanArticleFragment.class ), Notice_Page( 13, NoticeFragment.class ), YiGouMai_Page(
			14, HadBuyedFragment.class ), WriteComments_Page( 15,
			WriteCommentsFragment.class ), LookComments_Page( 16,
			LookCommentsFragment.class ), Collection_Page( 17,
			CollectionFragment.class );

	private Class< ? > clazz;
	private int value;

	FragmentPages( int value, Class< ? > cls )
	{
		this.clazz = cls;
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public Class< ? > getClazz()
	{
		return clazz;
	}

	public static Class< ? > getPageByValue( int value )
	{
		for ( FragmentPages p : values() )
		{
			if ( p.getValue() == value )
			{
				return p.getClazz();
			}
		}
		return null;
	}
}
