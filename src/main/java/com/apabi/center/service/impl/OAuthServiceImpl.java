package com.apabi.center.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.apabi.center.dao.ClientMapper;
import com.apabi.center.dao.UserMapper;
import com.apabi.center.entity.LocalUser;
import com.apabi.center.service.CacheService;
import com.apabi.center.service.OAuthService;
import com.apabi.center.util.MD5Encryption;

@Service
public class OAuthServiceImpl implements OAuthService {
	@Autowired
	private ClientMapper clientMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CacheService cacheService;
	
	@Override
	public LocalUser findLocalUserByCert(String cert) {
		LocalUser localUser = userMapper.findLocalUserBySalt(cert);
		
		if (localUser == null) {
			return null;
		}
		return localUser;
	}

	@Override
	public boolean checkClientByIdURI(String clientId, String redirectURI) {
		if (clientMapper.findClientByIdURI(clientId, redirectURI) == null) {
			return false;
		}
		return true;
	}

	@Override
	public LocalUser findLocalUserByEmailPassword(String email, String password) {
		LocalUser localUser = new LocalUser();
		localUser = userMapper.findLocalUserByEmail(email);
		String salt = localUser.getSalt();
		String savedPassword = localUser.getPassword();
		String encryptedPassword = null;
		String initPassword = password + salt;
		try {
			encryptedPassword = MD5Encryption.encryption(initPassword);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (savedPassword.equals(encryptedPassword)) {
			return localUser;
		}
		
		return null;
	}

	@Override
	public void addOAuthCode(String code, int uid) {
		String uidStr = String.valueOf(uid);
		cacheService.addAuthCode(code, uidStr);
	}

	@Override
	public boolean checkClientByIdSecretURI(String clientId, String clientSecret, String redirectURI) {
		if (clientMapper.findClientByIdSecretURI(clientId, clientSecret, redirectURI) == null) {
			return false;
		}
		return true;
	}

	@Override
	public int getUidByCode(String code) {
		String uidStr = cacheService.getUidByAuthCode(code);
		if (!StringUtils.isEmpty(uidStr)) {
			int uid = Integer.parseInt(uidStr);
			return uid;
		}
		return 0;
	}

	@Override
	public void addAccessToken(String accessToken, int uid) {
		String uidStr = String.valueOf(uid);
		cacheService.addAccessToekn(accessToken, uidStr);
		
	}


}
