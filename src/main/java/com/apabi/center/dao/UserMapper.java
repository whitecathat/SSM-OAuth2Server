package com.apabi.center.dao;


import com.apabi.center.entity.LocalUser;
import com.apabi.center.entity.User;

public interface UserMapper {
	void saveUser(User user);
	void saveLocalUser(LocalUser localUser);
}
