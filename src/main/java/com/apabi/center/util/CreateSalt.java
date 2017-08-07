package com.apabi.center.util;

import java.util.UUID;

public class CreateSalt {

	public static String getSalt() {
		String salt = UUID.randomUUID().toString();
		return salt;
	}
}
