package com.ipbase.bean;

import java.io.Serializable;

/**
 * 	
 * ClassName: Article 
 * @Description: 文章
 * @author kesar
 * @date 2015-10-28
 */
public class Article implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2623548831745508342L;
	
	private String id;
	private String title;
	private String user_id;
	private String author;
	private String journal_id;
	private String digest; // 摘要
	private String content; // 内容，安卓端就不用这个字段
	private int state;
	private String createtime;

	public Article() {
	}

	public String getId()
	{
		return id;
	}

	public String getTitle()
	{
		return title;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public String getAuthor()
	{
		return author;
	}

	public String getJournal_id()
	{
		return journal_id;
	}

	public String getDigest()
	{
		return digest;
	}
	
	public String getContent()
	{
		return content;
	}

	public int getState()
	{
		return state;
	}

	public String getCreatetime()
	{
		return createtime;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setUser_id(String userId)
	{
		user_id = userId;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public void setJournal_id(String journalId)
	{
		journal_id = journalId;
	}

	public void setDigest(String digest)
	{
		this.digest = digest;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
	}

	@Override
	public String toString()
	{
		return "Article [id=" + id + ", title=" + title + ", user_id="
				+ user_id + ", author=" + author + ", journal_id=" + journal_id
				+ ", digest=" + digest + ", content=" + content + ", state="
				+ state + ", createtime=" + createtime + "]";
	}
	
/*	
 * 继承Parcelable实现的方法
 * @Override
	public int describeContents() {
		return 0;
	}

	//该方法将类的数据写入外部提供的Parcel中。
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(user_id);
		dest.writeString(author);
		dest.writeString(journal_id);
		dest.writeString(content);
		dest.writeInt(state);
		dest.writeString(createtime);
	}
	
	 public static final Parcelable.Creator<CopyOfArticle> CREATOR  = new Creator<CopyOfArticle>() {  
	        //实现从source中创建出类的实例的功能  
	        @Override  
	        public CopyOfArticle createFromParcel(Parcel source) {  
	        	CopyOfArticle article  = new CopyOfArticle();  
	        	article.id = source.readString();  
	        	article.title= source.readString();  
	        	article.user_id = source.readString();  
	        	article.author = source.readString();  
	        	article.journal_id = source.readString();  
	        	article.content = source.readString();  
	        	article.state = source.readInt();  
	        	article.createtime = source.readString();  
	            return article;  
	        }  
	        //创建一个类型为T，长度为size的数组  
	        @Override  
	        public CopyOfArticle[] newArray(int size) {  
	            return new CopyOfArticle[size];  
	        }  
	    }; */

}
