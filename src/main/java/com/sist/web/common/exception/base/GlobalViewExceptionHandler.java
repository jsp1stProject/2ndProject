package com.sist.web.common.exception.base;


import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class GlobalViewExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ModelAndView handleViewException(Exception ex, HttpServletRequest request) {
		log.error("View 예외 발생: {}", ex.getMessage(), ex);
		
		ModelAndView mv = new ModelAndView("main/error");
		mv.addObject("error", "예상치 못한 에러 발생");
		mv.addObject("message", ex.getMessage());
		mv.addObject("status", 500);
		return mv;
	}
}
