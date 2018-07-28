package com.micro.auth.pojo;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.validation.constraints.Pattern;


public class User {
private String userName;
private String password;
@NotNull
//@Pattern(regexp="^([a-zA-Z0-9\\-\\.\\_]+)'+'(\\@)([a-zA-Z0-9\\-\\.]+)'+'(\\.)([a-zA-Z]{2,4})$",message="Enter Valid Email ID")
private String email;
private String mobileNo;
private String name;
private String id;
private String JWToken;
private Map<String,Object> roles;
public String getJWToken() {
	return JWToken;
}
public void setJWToken(String JWToken) {
	this.JWToken = JWToken;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getMobileNo() {
	return mobileNo;
}
public void setMobileNo(String mobileNo) {
	this.mobileNo = mobileNo;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}


public Map<String, Object> getRoles() {
	return roles;
}
public void setRoles(Map<String, Object> roles) {
	this.roles = roles;
}

public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}

}
