package com.bridgelabz.fundoonotes.user.model;

import org.springframework.data.annotation.Id;

public class LoginDTO {
	@Id
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
