package com.nfactorial.auth.services;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nfactorial.auth.dao.RedisDao;
import com.nfactorial.auth.pojo.User;



@Component
public class AuthServiceImpl implements AuthService {

	@Autowired
	RedisDao redisDao;
	
	
	@Override
	public String createUser(User user) {
		return redisDao.createUser(user);
		
	}

	@Override
	public String updateUser(User user) {
		return redisDao.updateUser(user);
	}

	@Override
	public User getUserByUserName(String userName) {
		Map<String, String> userMap=redisDao.getUser(userName);
		ObjectMapper mapper= new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
		User user=mapper.convertValue(userMap, User.class);
		return user;
	}

	@Override
	public String deleteUser(String id) {
		return redisDao.deleteUser(id);
	}

	
}
