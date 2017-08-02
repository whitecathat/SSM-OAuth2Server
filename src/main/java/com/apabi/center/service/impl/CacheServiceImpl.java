package com.apabi.center.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.apabi.center.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService{

	private Cache cache;
	
	@Autowired
	public CacheServiceImpl(CacheManager cacheManager){
		this.cache = cacheManager.getCache("code-cache");
	}
	
	@Override
	public void addAuthCode(String authCode, String username) {
		cache.put(authCode, username);
	}

	@Override
	public String getUsernameByAuthCode(String authCode) {
		return (String)cache.get(authCode).get();
	}

}
