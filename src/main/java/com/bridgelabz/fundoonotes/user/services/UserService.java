package com.bridgelabz.fundoonotes.user.services;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.user.exception.ActivationException;
import com.bridgelabz.fundoonotes.user.exception.ChangePassException;
import com.bridgelabz.fundoonotes.user.exception.LoginException;
import com.bridgelabz.fundoonotes.user.exception.MalformedUUIDException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.user.model.ChangePassDTO;
import com.bridgelabz.fundoonotes.user.model.LoginDTO;
import com.bridgelabz.fundoonotes.user.model.MailUser;
import com.bridgelabz.fundoonotes.user.model.RegistrationDTO;

public interface UserService {

	public String login(LoginDTO logUser) throws LoginException, MessagingException;

	public String register(RegistrationDTO regUser) throws RegistrationException, MessagingException;

	public void activateUser(String token) throws ActivationException, TokenParsingException;

	public void changePassword(ChangePassDTO reset, String UUID)
			throws ChangePassException, MessagingException, ActivationException, TokenParsingException, MalformedUUIDException;

	public void forgetPassword(MailUser mail) throws MessagingException, ChangePassException;

	public String addimage(String token, MultipartFile file) throws TokenParsingException, UserNotFoundException, IOException;

	public void removeImage(String token,String fileName) throws TokenParsingException, UserNotFoundException;

}
