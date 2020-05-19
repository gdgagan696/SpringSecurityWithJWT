package com.demo.SpringSecurityJwt.controllers;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.demo.SpringSecurityJwt.exception.CustomException;

@ControllerAdvice
public class ExceptionController {

	private final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);

	final String ERROR_GENRIC="Please Try Again After Sometime.If error persists then contact admin for support.";
	final String ERROR_EMAIL="E-mail Id not unique.";
	final String ERROR_REG_NO="Registration number not unique.";
	final String ERROR_USERNAME="Username not unique.";
	
	@ExceptionHandler(value = { CustomException.class })
	public ResponseEntity<Object> handleCustomException(final CustomException ex) {
		LOG.error("CustomException : ", ex);
		return new ResponseEntity<>(getMsgInMap(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(),
				HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex) {
		LOG.error("ConstraintViolationException : ", ex);
		String errorMsg=ERROR_GENRIC;
		if(StringUtils.startsWithIgnoreCase(ex.getConstraintName(), "email"))errorMsg=ERROR_EMAIL;
		if(StringUtils.startsWithIgnoreCase(ex.getConstraintName(), "reg_no"))errorMsg=ERROR_REG_NO;
		if(StringUtils.startsWithIgnoreCase(ex.getConstraintName(), "user_name"))errorMsg=ERROR_USERNAME;
		return new ResponseEntity<>(getMsgInMap(HttpStatus.BAD_REQUEST, errorMsg), new HttpHeaders(),
				HttpStatus.BAD_REQUEST);
	}
	
	private Object getMsgInMap(HttpStatus status, String errorMsg) {
		Map<String, Object> errorMap = new HashMap<>();
		errorMap.put("errorMsg", errorMsg);
		errorMap.put("errorStatus", status.toString());
		return errorMap;
	}

}
