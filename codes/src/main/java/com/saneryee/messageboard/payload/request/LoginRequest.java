package com.saneryee.messageboard.payload.request;

import javax.validation.constraints.NotBlank;


public class LoginRequest {
	@NotBlank
  private String username;

	@NotBlank
	private String password;

	private boolean rememberme;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getRememberme() {
		return rememberme;
	}
	public void setRememberme(Boolean rememberme){
		this.rememberme = rememberme;
	}
}
