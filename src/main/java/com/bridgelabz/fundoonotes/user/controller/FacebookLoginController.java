package com.bridgelabz.fundoonotes.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.user.services.FacebookLoginService;
import com.bridgelabz.fundoonotes.user.services.SocialLoginService;

@RestController
@RequestMapping("/Login-with-Facebook")
public class FacebookLoginController {

	@Autowired
	SocialLoginService facebookService;
	
	@GetMapping("/createAuthorization")
    public String createFacebookAuthorization(){
        return facebookService.createAuthorizationUrl();
    }
	
	@GetMapping("/Login")
	public void createFacebookAccessToken(@RequestParam("code") String code){
	    facebookService.login(code);
	}
	
}
