package com.example.securityDemo.jwt;

import java.util.List;

public class LoginResponse {

	private String jwToken;
	private String username;
	private List<String> roles;
	
	
	public LoginResponse(String jwToken, String username, List<String> roles) {
		super();
		this.jwToken = jwToken;
		this.username = username;
		this.roles = roles;
	}


	public String getJwToken() {
		return jwToken;
	}


	public void setJwToken(String jwToken) {
		this.jwToken = jwToken;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public List<String> getRoles() {
		return roles;
	}


	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
	
}
