package com.micro.auth.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.micro.auth.services.AuthService;


@RequestMapping(value="/authserver")
public class AuthContollerImpl {
	
	
	@Autowired
	AuthService authService;
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/getPublicKey",method = RequestMethod.GET)
	
	public ResponseEntity<String> getPublicKey() {
		return ResponseEntity.ok(authService.getPublicKey());
	}
	
}
