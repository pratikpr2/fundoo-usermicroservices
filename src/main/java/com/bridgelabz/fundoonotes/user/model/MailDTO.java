package com.bridgelabz.fundoonotes.user.model;

import java.io.Serializable;

public class MailDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String email;
	
	private String subject;
	
	private String body;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "MailDTO [email=" + email + ", subject=" + subject + ", body=" + body + "]";
	}
	
	
	
}
