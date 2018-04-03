package com.nfactorial.auth.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.nfactorial.auth.pojo.User;
import com.nfactorial.auth.redis.RedisConnection;

@Component
public class RedisDaoImpl implements RedisDao {

	@Autowired
	RedisConnection redisConnection;
	
	
	@Override
	public String createUser(User user) {
	try(StatefulRedisConnection<String, String> connection = redisConnection.getPool().borrowObject()){
		
		RedisCommands<String, String> commands = connection.sync();
		ObjectMapper mapper= new ObjectMapper();
		Map<String, String> userMap=mapper.convertValue(user, Map.class);
		return commands.hmset("user:"+user.getUserName(), userMap);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return null;
	}

	@Override
	public String updateUser(User user) {
		try(StatefulRedisConnection<String, String> connection = redisConnection.getPool().borrowObject()){
			
			RedisCommands<String, String> commands = connection.sync();
			ObjectMapper mapper= new ObjectMapper();
			Map<String, String> userMap=mapper.convertValue(user, Map.class);
			return commands.hmset("user:"+user.getUserName(), userMap);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
	}

	@Override
	public Map<String, String> getUser(String userName) {
		try(StatefulRedisConnection<String, String> connection = redisConnection.getPool().borrowObject()){
			RedisCommands<String, String> commands = connection.sync();
			return commands.hgetall("user:"+userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String deleteUser(String userName) {
		try(StatefulRedisConnection<String, String> connection = redisConnection.getPool().borrowObject()){
			RedisCommands<String, String> commands = connection.sync();
			 return ""+commands.del("user:"+userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
}
