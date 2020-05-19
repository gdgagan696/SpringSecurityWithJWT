package com.demo.SpringSecurityJwt.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.SpringSecurityJwt.dto.ResponseDto;
import com.demo.SpringSecurityJwt.dto.TokenResponseDto;
import com.demo.SpringSecurityJwt.exception.CustomException;
import com.demo.SpringSecurityJwt.model.AppUser;
import com.demo.SpringSecurityJwt.services.CustomUserDetailsServiceImpl;
import com.demo.SpringSecurityJwt.util.JwtUtil;

@RestController
@RequestMapping("/users")
public class UserController {
	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/authenticate")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<TokenResponseDto> authenticateUser(@RequestBody(required = true) AppUser appUser) throws Exception {
		Authentication authentication=null;
		try {
			authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUserName(),appUser.getPassword(),Collections.emptyList()));
			
		} catch (BadCredentialsException e) {
			throw new CustomException("Bad Credentials,Invalid Password.");
		}
//		final UserDetails userDetails=customUserDetailsService.loadUserByUsername(appUser.getUserName());
		final UserDetails userDetails=(User) authentication.getPrincipal();
		final String jwtToken=jwtUtil.generateToken(userDetails);
		final AppUser loggedInUser=customUserDetailsService.getUser(userDetails.getUsername());
		return new ResponseEntity<>(new TokenResponseDto(jwtToken,loggedInUser),HttpStatus.OK);
	}
	
	@PostMapping("/createUser")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseDto> signUp(@RequestBody(required = true) AppUser user) throws CustomException{
		String msg="User Registered Successfully.";
		customUserDetailsService.saveNewUser(user);
		return new ResponseEntity<>(new ResponseDto(msg),HttpStatus.CREATED);
	}
	
	@GetMapping("/getUser")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<AppUser> getUserInfo(@AuthenticationPrincipal Principal principal) throws CustomException{
		return new ResponseEntity<>(customUserDetailsService.getUser(principal.getName()),HttpStatus.OK);
	}
	
	@PutMapping("/changePassword")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseDto> changePassword(@AuthenticationPrincipal Principal principal,@RequestBody(required = true) Map<String, String> password) throws CustomException{
		String msg="Password Updated Successfully.";
		customUserDetailsService.updatePassword(principal.getName(), password);
		return new ResponseEntity<>(new ResponseDto(msg),HttpStatus.OK);
	}
	
	
}
