package com.micro.auth.rest;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.auth.pojo.User;


public interface AuthController {

public Response login(User user);
public Response register(User user);
public Response delete(String userName);
public Response update(User user);
public Response getPublicKey();

}