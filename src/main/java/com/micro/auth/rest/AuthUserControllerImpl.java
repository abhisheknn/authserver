package com.micro.auth.rest;

import java.net.URI;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.micro.auth.exception.UserNotFoundException;
import com.micro.auth.pojo.User;
import com.micro.auth.services.AuthService;

@RestController
@RequestMapping("/user")
public class AuthUserControllerImpl implements AuthUserController {
	
	@Autowired
	AuthService authService;
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/login",method = RequestMethod.POST)
	@Override
	public Response login(@RequestBody User user) {
		return Response.ok(authService.login(user.getUserName(),user.getPassword())).build();
	}
	
	@CrossOrigin(origins="*")
	@GetMapping("/{username}")
	public User retriveUser(@PathVariable String username) {
		User user = authService.getUserByUserName(username);
		if(user==null) {
			throw new UserNotFoundException("User not found with "+ username);
		}
		
		return user;
		
			
	}
	
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/register",method = RequestMethod.POST)
	//@Override
	public ResponseEntity register( @Valid @RequestBody User user) {
		authService.createUser(user);
		URI location =ServletUriComponentsBuilder.fromCurrentRequest().path("{username}").buildAndExpand(user.getUserName()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
	@Override
	public Response update(@RequestBody User user) {
		return Response.ok(authService.updateUser(user)).build();
	}
	
	@RequestMapping(value="/delete/{userName}",method = RequestMethod.DELETE)
	@Override
	public Response delete(@PathVariable("userName") String userName) {
		return Response.ok(authService.deleteUser(userName)).build();
	}
	
	
	@RequestMapping(value="/refreshToken",method = RequestMethod.POST)
	@Override
	public Response refreshToken(@RequestBody User user) {
		return Response.ok(authService.refreshToken(user.getUserName())).build();
	}
}
