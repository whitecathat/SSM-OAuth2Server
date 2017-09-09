package com.apabi.center.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.apabi.center.consts.UserConsts;
import com.apabi.center.dao.UserMapper;
import com.apabi.center.entity.LocalUser;
import com.apabi.center.entity.User;
import com.apabi.center.service.UserService;
import com.apabi.center.util.CreateSalt;
import com.apabi.center.util.MD5Encryption;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	
	@Override
	@Transactional
	public boolean saveUser(String name, String email, String password) {
		User user = new User();
		LocalUser localUser = new LocalUser();
		String salt = CreateSalt.getSalt();
		String encryptedPassword = null;
		String initPassword = password + salt;
		try {
			encryptedPassword = MD5Encryption.encryption(initPassword);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
		user.setName(name);
		user.setStatus(UserConsts.UNACTIVATED_STATUS);
		
		userMapper.saveUser(user);
		
		localUser.setUid(user.getUid());
		localUser.setEmail(email);
		localUser.setSalt(salt);
		
		if (!StringUtils.isEmpty(encryptedPassword)) {
			localUser.setPassword(encryptedPassword);
			userMapper.saveLocalUser(localUser);
		} else {
			return false;
		}
		
		return true;
	}

}
