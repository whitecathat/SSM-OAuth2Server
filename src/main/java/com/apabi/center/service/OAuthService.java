package com.apabi.center.service;

import com.apabi.center.entity.LocalUser;

public interface OAuthService {
	
	LocalUser findLocalUserByCert(String cert);
	boolean checkClientByIdURI(String clientId, String redirectURI);
	LocalUser findLocalUserByEmailPassword(String email, String password);
}
