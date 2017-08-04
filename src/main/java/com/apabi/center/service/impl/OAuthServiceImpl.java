package com.apabi.center.service.impl;

import org.springframework.stereotype.Service;

import com.apabi.center.service.OAuthService;

@Service
public class OAuthServiceImpl implements OAuthService {

	@Override
	public boolean checkUserCookie(String token) {
		return false;
	}

}
