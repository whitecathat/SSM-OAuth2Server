package com.apabi.center.dao;


import org.apache.ibatis.annotations.Param;

import com.apabi.center.entity.LocalUser;
import com.apabi.center.entity.User;

public interface UserMapper {
	void saveUser(User user);
	void saveLocalUser(LocalUser localUser);
	LocalUser findLocalUserBySalt(@Param("salt") String salt);
	LocalUser findLocalUserByEmail(@Param("email") String email);
}
