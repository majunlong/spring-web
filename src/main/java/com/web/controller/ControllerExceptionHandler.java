package com.web.controller;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.web.support.exception.NotLoginedException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler({ NotLoginedException.class })
	public ModelAndView notLoginedException(Exception e) {
		ModelAndView model = new ModelAndView("/index");
		model.addObject("error", e);
		return model;
	}

	@ExceptionHandler({ Exception.class })
	public ModelAndView exception(Exception e) {
		ModelAndView model = new ModelAndView("/error/index");
		model.addObject("error", e);
		return model;
	}

	@MessageExceptionHandler({ Exception.class })
	public void handleException(Exception e) {
		e.printStackTrace();
	}

}
