package com.demo.SpringSecurityJwt.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.SpringSecurityJwt.dto.ResponseDto;

@RestController
@RequestMapping("/test")
public class TestController {

	
	@GetMapping(path = "/hello",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseDto> getHello() {
		return new ResponseEntity<>(new ResponseDto("Hello from API"), HttpStatus.OK);
	}
	
	
}
