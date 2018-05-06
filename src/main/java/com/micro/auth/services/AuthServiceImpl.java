package com.micro.auth.services;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.auth.constant.AppConstants;
import com.micro.auth.dao.RedisDao;
import com.micro.auth.pojo.User;
import com.micro.auth.util.JWTProvider;
import com.micro.auth.util.KeyProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Component
public class AuthServiceImpl implements AuthService {

	@Autowired
	RedisDao redisDao;

	@Autowired
	JWTProvider jwtProvider;

	@Autowired
	KeyProvider keyProvider;

	@Override
	public String createUser(User user) {
		// For the new users roles will not available
		if (user.getRoles() == null) {
			Map<String, Object> defaultAccess = new HashMap<>();
			defaultAccess.put("accessType", "default");
			user.setRoles(defaultAccess);
		}
		if (user.getJWToken() == null) {
			String token = jwtProvider.getToken(user.getUserName(), user.getRoles());
			user.setJWToken(token);
		}
		return redisDao.createUser(user);
	}

	@Override
	public String updateUser(User user) {
		return redisDao.updateUser(user);
	}

	@Override
	public User getUserByUserName(String userName) {
		Map<String, String> userMap = redisDao.getUser(userName);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		User user = mapper.convertValue(userMap, User.class);
		return user;
	}

	@Override
	public String deleteUser(String id) {
		return redisDao.deleteUser(id);
	}

	private Key getKey() {
		Key k = keyProvider.getPublicKey(AppConstants.CERTALIAS);
		if (k == null) {
			keyProvider.createAndStoreCert("cn=unknown", AppConstants.CERTALIAS); // get this from configuration service
			k = keyProvider.getPublicKey(AppConstants.CERTALIAS); // get this from configuration service
		}
		return k;
	}

	@Override
	public String getPublicKey() {
		Key k = getKey();
		String stringKey = Base64.getEncoder().encodeToString(k.getEncoded());
		return stringKey;
	}

	private Map<String, Object> convertStringToMap(String userClaims) {
		Map<String, Object> userClaimsMap = new HashMap<>();
		userClaims = userClaims.substring(1, userClaims.length() - 1);
		String[] keyValuePairs = userClaims.split(",");
		for (String keyValue : keyValuePairs) {
			String[] entry = keyValue.split("=");
			userClaimsMap.put(entry[0], entry[1]);
		}
		return userClaimsMap;
	}

	@Override
	public String login(String userName, String password) {
		Map<String, String> userMap = redisDao.getUser(userName);
		String jwToken = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// User user=mapper.convertValue(userMap, User.class);
		if (userMap!=null && userMap.get(AppConstants.USERNAME).equals(userName)
				&& userMap.get(AppConstants.PASSWORD).equals(password)) {
			Key k = getKey();
			jwToken = userMap.get(AppConstants.JWTOKEN);
			try {
				Claims claims = Jwts.parser().setSigningKey(k).parseClaimsJws(jwToken).getBody();
			} catch (ExpiredJwtException e) {
				String userClaims = userMap.get("roles");
				String token = jwtProvider.getToken(userMap.get(AppConstants.USERNAME), convertStringToMap(userClaims));
				userMap.put(AppConstants.JWTOKEN, token);
			}
		}
		return userMap.get(AppConstants.JWTOKEN);
	}

	@Override
	public String refreshToken(String userName) {
		Map<String, String> userMap = redisDao.getUser(userName);
		String jwToken = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// User user=mapper.convertValue(userMap, User.class);
		if (userMap.get(AppConstants.USERNAME).equals(userName)) {
			Key k = getKey();
			jwToken = userMap.get(AppConstants.JWTOKEN);
			String userClaims = userMap.get("roles");
			String token = jwtProvider.getToken(userMap.get(AppConstants.USERNAME), convertStringToMap(userClaims));
			userMap.put(AppConstants.JWTOKEN, token);
		}
		return userMap.get(AppConstants.JWTOKEN);
	}

}
