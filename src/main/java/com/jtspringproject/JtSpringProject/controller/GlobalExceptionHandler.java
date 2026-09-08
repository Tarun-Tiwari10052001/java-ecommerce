package com.jtspringproject.JtSpringProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Nothing in this app previously caught anything - a bad DB call or a
 * missing record surfaced as a raw stack trace / Whitelabel page. This gives
 * every controller a consistent, user-facing fallback.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ModelAndView handleIllegalState(IllegalStateException ex) {
		log.warn("Rejected request: {}", ex.getMessage());
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("msg", ex.getMessage());
		return mv;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleUnexpected(Exception ex) {
		log.error("Unhandled exception", ex);
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("msg", "Something went wrong. Please try again.");
		return mv;
	}
}
