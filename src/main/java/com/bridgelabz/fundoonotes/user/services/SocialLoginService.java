package com.bridgelabz.fundoonotes.user.services;

public interface SocialLoginService {

	String createAuthorizationUrl();
	String login(String code);
}
