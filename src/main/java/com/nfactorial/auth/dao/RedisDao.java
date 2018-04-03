package com.nfactorial.auth.dao;

import java.util.Map;

import com.nfactorial.auth.pojo.User;

public interface RedisDao {

	public String createUser(User user);
	public String updateUser(User user);
	public Map<String, String> getUser(String id);
	public String deleteUser(String id);
}
