package com.apabi.center.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apabi.center.service.CacheService;

@Controller
public class UserController {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private CacheService cacheService;
	
	@RequestMapping("/index")
	public String index(Model model){
		String name = null;
//		logger.debug("recive the request");
		cacheService.addAuthCode("123", "tom");
		name = cacheService.getUsernameByAuthCode("123");
		model.addAttribute("username", name);
		return "index";
	}
}
