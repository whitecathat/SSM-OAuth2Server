package com.apabi.center.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.apabi.center.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/register")
	public String login(String message, Model model) {
		model.addAttribute("message", message);
		return "register";
	}
	
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public String check(String name, String email, String password, String repassword) {
		String message = null;
		
		if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(email) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(repassword)) {
			if (password.equals(repassword)) {
				if (userService.saveUser(name, email, password) == true) {
					return "redirect:/user/success";
				}
				message = "system has error";
			} else {
				message = "password is different";
			}
		} else {
			message = "have null";
		}
		return "redirect:/user/register?message=" + message;
	}
	
	@RequestMapping("/success")
	public String success() {
		return "success";
	}
}
