package com.micro.auth.util;

import java.security.Key;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.auth.constant.AppConstants;
import com.micro.auth.pojo.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTProvider {
	@Autowired
	KeyProvider keyProvider;

	public String getToken(String userName, Map<String, Object>   claims) {
		String s = null;
		Calendar expires = Calendar.getInstance();
		expires.roll(Calendar.HOUR, 2);
		try {

			Key key = keyProvider.getPrivateKey(AppConstants.CERTALIAS); // Get this form configuration server
			s = Jwts.builder()
					.setClaims(claims)
					.setSubject(userName)	
					.setIssuedAt(new Date())
					.setExpiration(expires.getTime())
					.signWith(SignatureAlgorithm.RS256, key).compact();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
