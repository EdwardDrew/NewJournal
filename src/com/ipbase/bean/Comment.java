package com.ipbase.bean;

/**
 * 
 * 
 * Comment 评论或者是回复
 * 
 * @author kesar
 * 
 *         2015-8-15 下午11:57:48
 * 
 * @version 1.0.0
 * 
 */
public class Comment
{
	private String id;
	private String user_id;
	private String nickname;
	private String article_id;
	private String comment_id;
	private String content;
	private int type;
	private String createtime;

	public String getUserName()
	{
		return nickname;
	}

	public void setUserName( String nickname )
	{
		this.nickname = nickname;
	}

	public String getArticle_id()
	{
		return article_id;
	}

	public void setArticle_id( String article_id )
	{
		this.article_id = article_id;
	}

	public String getId()
	{
		return id;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public String getContent()
	{
		return content;
	}

	public int getType()
	{
		return type;
	}

	public String getCreatetime()
	{
		return createtime;
	}

	public void setId( String id )
	{
		this.id = id;
	}

	public void setUser_id( String userId )
	{
		user_id = userId;
	}

	public void setContent( String content )
	{
		this.content = content;
	}

	public void setType( int type )
	{
		this.type = type;
	}

	public void setCreatetime( String createtime )
	{
		this.createtime = createtime;
	}

	public String getComment_id()
	{
		return comment_id;
	}

	public void setComment_id( String commentId )
	{
		comment_id = commentId;
	}

	@ Override
	public String toString()
	{
		return "Comment [comment_id=" + comment_id + ", content=" + content
				+ ", createtime=" + createtime + ", id=" + id + ", type="
				+ type + ", user_id=" + user_id + ", user_name=" + nickname
				+ ", article_id=" + article_id + "]";
	}

}
