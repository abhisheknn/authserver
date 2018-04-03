package com.nfactorial.auth.pojo;

import javax.validation.constraints.Null;
import javax.ws.rs.DefaultValue;

public class User {
private String userName;
private String password;
private String email;
private String mobileNo;
private String name;
private String id;
private String jwtToken;

public String getJwtToken() {
	return jwtToken;
}
public void setJwtToken(String jwtToken) {
	this.jwtToken = jwtToken;
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

String[] roles;

public String[] getRoles() {
	return roles;
}
public void setRoles(String[] roles) {
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
