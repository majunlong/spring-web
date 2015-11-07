package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.web.support.annotation.Login;

@Login
@Controller
public class URIController {

	@RequestMapping(path = "/websocket", method = RequestMethod.GET)
	public String websocket() {
		return "/websocket/index";
	}

	@RequestMapping(path = "/websocket/index", method = RequestMethod.GET)
	public String websocketIndex() {
		return "/websocket/index";
	}

}
