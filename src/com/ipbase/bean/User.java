package com.ipbase.bean;

/**
 * 
 * ClassName: User
 * 
 * @Description: 用户
 * @author kesar
 * @date 2015年9月23日
 */
public class User
{
	private String id;
	private String phone;
	private String password;
	private String nickname;
	private String email;
	private String weixin;
	private String aipay;

	public String getId()
	{
		return id;
	}

	public String getPhone()
	{
		return phone;
	}

	public String getPassword()
	{
		return password;
	}

	public String getNickname()
	{
		return nickname;
	}

	public String getEmail()
	{
		return email;
	}

	public String getWeixin()
	{
		return weixin;
	}

	public String getAipay()
	{
		return aipay;
	}

	public void setId( String id )
	{
		this.id = id;
	}

	public void setPhone( String phone )
	{
		this.phone = phone;
	}

	public void setPassword( String password )
	{
		this.password = password;
	}

	public void setNickname( String nickname )
	{
		this.nickname = nickname;
	}

	public void setEmail( String email )
	{
		this.email = email;
	}

	public void setWeixin( String weixin )
	{
		this.weixin = weixin;
	}

	public void setAipay( String aipay )
	{
		this.aipay = aipay;
	}

	@ Override
	public String toString()
	{
		return "User [id=" + id + ", phone=" + phone + ", password=" + password
				+ ", nickname=" + nickname + ", email=" + email + ", weixin="
				+ weixin + ", aipay=" + aipay + "]";
	}
};