package com.demo.SpringSecurityJwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.SpringSecurityJwt.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
	
	Optional<AppUser> findByUserName(String userName);

}
