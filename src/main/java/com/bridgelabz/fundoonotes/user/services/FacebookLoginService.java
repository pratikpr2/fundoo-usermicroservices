package com.bridgelabz.fundoonotes.user.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
public class FacebookLoginService {

	@Value("${spring.social.facebook.appId}")
	String facebookAppId;
	@Value("${spring.social.facebook.appSecret}")
	String facebookSecret;
	
	String accessToken;

	public String createFacebookAuthorizationURL() {
		FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
		OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
		OAuth2Parameters params = new OAuth2Parameters();
		params.setRedirectUri("http://localhost:8080/swagger-ui.html#!/facebook-login-controller/createFacebookAccessTokenUsingGET");
		params.setScope("public_profile,email,user_birthday");
		return oauthOperations.buildAuthorizeUrl(params);
	}
	
	public void createFacebookAccessToken(String code) {
	    FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
	    AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, "http://localhost:8080/swagger-ui.html#!/facebook-login-controller/createFacebookAccessTokenUsingGET", null);
	    accessToken = accessGrant.getAccessToken();
	}

	public String getName() {
	    Facebook facebook = new FacebookTemplate(accessToken);
	    return facebook.fetchObject("me",String.class,"email");
	}
}
