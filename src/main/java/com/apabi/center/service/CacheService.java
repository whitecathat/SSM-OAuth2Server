package com.apabi.center.service;

public interface CacheService {

	public void addAuthCode(String authCode, String uid);
	public String getUidByAuthCode(String authCode);
	public void addAccessToekn(String accessToken, String uid);
}
