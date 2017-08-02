package com.apabi.center.service;

public interface CacheService {

	public void addAuthCode(String authCode, String username);
	public String getUsernameByAuthCode(String authCode);
}
