package com.apabi.center.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Encryption {
	
	public static String encryption(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
			MessageDigest md5=MessageDigest.getInstance("MD5");
			String encryptStr = md5.digest(str.getBytes("UTF-8")).toString();
			return encryptStr;
	}
}
