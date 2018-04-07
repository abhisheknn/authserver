package com.nfactorial.auth.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nfactorial.auth.pojo.User;
import com.nfactorial.auth.services.AuthService;

@RestController
@RequestMapping("/authserver")
public class AuthControllerImpl implements AuthController {
	
	@Autowired
	AuthService authService;
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	@Override
	public Response login(@RequestBody User user) {
		
		return Response.ok(authService.login(user.getUserName(),user.getPassword())).build();
	}
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	@Override
	public Response register(@RequestBody User user) {
	
		return Response.ok(authService.createUser(user)).build();
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
	
	
	@RequestMapping(value="/getPublickey",method = RequestMethod.GET)
	@Override
	public Response getPublicKey() {
	
		return Response.ok(authService.getPublicKey()).build();
	}
}
