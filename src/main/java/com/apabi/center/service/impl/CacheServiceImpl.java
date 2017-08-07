package com.apabi.center.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.apabi.center.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {

	private Cache cache;
	
	@Autowired
	public CacheServiceImpl(CacheManager cacheManager) {
		this.cache = cacheManager.getCache("code-cache");
	}
	
	@Override
	public void addAuthCode(String authCode, String uid) {
		cache.put(authCode, uid);
	}

	@Override
	public String getUidByAuthCode(String authCode) {
		return (String)cache.get(authCode).get();
	}

	@Override
	public void addAccessToekn(String accessToken, String uid) {
		cache.put(accessToken, uid);
	}

}
