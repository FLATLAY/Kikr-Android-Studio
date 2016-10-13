package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.SocialFriend;

public class FbTwFriendsRes {

	String code;
	String message;
	List<SocialFriend> twitter_friend_list;
	List<SocialFriend> fb_friend_list;
	List<SocialFriend> followers_list;
	
	/**
	 * @return the followers_list
	 */
	public List<SocialFriend> getFollowers_list() {
		return followers_list;
	}
	/**
	 * @param followers_list the followers_list to set
	 */
	public void setFollowers_list(List<SocialFriend> followers_list) {
		this.followers_list = followers_list;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the twitter_friend_list
	 */
	public List<SocialFriend> getTwitter_friend_list() {
		return twitter_friend_list;
	}
	/**
	 * @param twitter_friend_list the twitter_friend_list to set
	 */
	public void setTwitter_friend_list(List<SocialFriend> twitter_friend_list) {
		this.twitter_friend_list = twitter_friend_list;
	}
	/**
	 * @return the fb_friend_list
	 */
	public List<SocialFriend> getFb_friend_list() {
		return fb_friend_list;
	}
	/**
	 * @param fb_friend_list the fb_friend_list to set
	 */
	public void setFb_friend_list(List<SocialFriend> fb_friend_list) {
		this.fb_friend_list = fb_friend_list;
	}
	
	
}
