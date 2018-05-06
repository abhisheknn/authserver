package com.micro.auth.services;

import com.micro.auth.pojo.User;

public interface AuthService {
	public String createUser(User user);
	public String updateUser(User user);
	public User getUserByUserName(String userName);
	public String deleteUser(String id);
	public String login(String userName,String password);
	public String getPublicKey();
	public String refreshToken(String userName);
}
