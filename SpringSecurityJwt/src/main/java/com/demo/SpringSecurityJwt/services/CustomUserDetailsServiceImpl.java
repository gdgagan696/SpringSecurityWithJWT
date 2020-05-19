package com.demo.SpringSecurityJwt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.SpringSecurityJwt.exception.CustomException;
import com.demo.SpringSecurityJwt.model.AppUser;
import com.demo.SpringSecurityJwt.repository.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<AppUser> appUser=userRepository.findByUserName(username);
		if(!appUser.isPresent()) {
			throw new CustomException("User with this username not registered.");
		}
		List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(appUser.get().getUserRole()));
		return new User(appUser.get().getUserName(), appUser.get().getPassword(),authorities);
	}
	
	public void saveNewUser(AppUser appUser) {
		validateUser(appUser);
		appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
		userRepository.save(appUser);
	}
	
	public AppUser getUser(String userName) throws UsernameNotFoundException {
		return userRepository.findByUserName(userName).get();
	}
	
	public void updatePassword(String userName,Map<String, String> password) throws UsernameNotFoundException {
		Optional<AppUser> loggedInUser=userRepository.findByUserName(userName);
		if(loggedInUser.isPresent()) {
			String oldPassword=password.get("oldPassword");
			String newPassword=password.get("newPassword");
			String reEnterPassword=password.get("rePassword");
			if(!newPassword.equals(reEnterPassword)) {
				throw new CustomException("New Password and re-entered password not matched.");
			}
			if(bCryptPasswordEncoder.matches(oldPassword,loggedInUser.get().getPassword())) {
				loggedInUser.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
				userRepository.save(loggedInUser.get());
			}
			else {
				throw new CustomException("Old Password not correct.");
			}
		}
		else {
			throw new CustomException("Invalid User,User does not exists.");
		}
	}
	
	private void validateUser(AppUser user) {
		if(Objects.isNull(user)) {
			throw new CustomException("User can not be empty.");
		}
		if(Objects.isNull(user.getFirstName())) {
			throw new CustomException("User first name can not be empty.");
		}
		if(Objects.isNull(user.getRegNo())) {
			throw new CustomException("User reg number can not be empty.");
		}
		if(Objects.isNull(user.getEmail())) {
			throw new CustomException("User email can not be empty.");
		}
		if(Objects.isNull(user.getUserName())) {
			throw new CustomException("Username can not be empty.");
		}
		if(Objects.isNull(user.getPassword())) {
			throw new CustomException("User password can not be empty.");
		}
		if(Objects.isNull(user.getUserRole())) {
			throw new CustomException("User role can not be empty.");
		}
	}
}
