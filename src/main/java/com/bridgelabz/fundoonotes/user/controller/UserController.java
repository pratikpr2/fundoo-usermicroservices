package com.bridgelabz.fundoonotes.user.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
import com.bridgelabz.fundoonotes.user.model.ResponseDto;
import com.bridgelabz.fundoonotes.user.services.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	// -------------------Login--------------------------

	/**
	 * @param checkUser
	 * @param response
	 * @return Login Response
	 * @throws LoginException
	 * @throws MessagingException 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> login(@RequestBody LoginDTO loginDto,HttpServletResponse res) throws LoginException, MessagingException {

		String token = userService.login(loginDto);
		
		System.out.println(token+"in user controller");
		
		res.setHeader("jwtoken",token);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("SuccessFully LoggedIn");
		response.setStatus(1);
		
		return new ResponseEntity<ResponseDto>(response, HttpStatus.OK);

	}

	// ----------------Register---------------------------

	/**
	 * @param regUser
	 * @return Registration Response
	 * @throws RegistrationException
	 * @throws MessagingException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> register(@RequestBody RegistrationDTO regDto,HttpServletResponse res)
			throws RegistrationException, MessagingException {

		String token =userService.register(regDto);

		res.setHeader("token", token);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("SuccessFully Registered");
		response.setStatus(1);
		
		return new ResponseEntity<ResponseDto>(response, HttpStatus.OK);
	}
	// -------------------Activate Account-------------------

	/**
	 * @param token
	 * @return Activation Response
	 * @throws RegistrationException
	 * @throws ActivationException
	 * @throws TokenParsingException
	 */
	@RequestMapping(value = "/activateaccount", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> activateaccount(@RequestParam(value = "token") String token)
			throws RegistrationException, ActivationException, TokenParsingException {
	
		userService.activateUser(token);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("Account Activated SuccesFully");
		response.setStatus(1);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	//----------------------Forget PassWord----------------------

	/**
	 * @param user
	 * @return Mail Response
	 * @throws ChangePassException
	 * @throws MessagingException
	 */
	@RequestMapping(value = "/forgetpassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> forgetPassword(@RequestBody MailUser user)
			throws ChangePassException, MessagingException {

		userService.forgetPassword(user);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("Please Check Mail To Confirm Changing Password");
		response.setStatus(2);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	//----------------------Reset PassWord------------------------

	/**
	 * @param reset
	 * @param token
	 * @return ResetPassword Response
	 * @throws ChangePassException
	 * @throws MessagingException
	 * @throws ActivationException
	 * @throws TokenParsingException
	 * @throws MalformedUUIDException
	 */
	@RequestMapping(value = "/resetpassword", method = RequestMethod.PUT)
	private ResponseEntity<ResponseDto> resetpassword(@RequestBody ChangePassDTO reset,
			@RequestParam(value = "token") String token)
			throws ChangePassException, MessagingException, ActivationException, TokenParsingException, MalformedUUIDException {

		userService.changePassword(reset, token);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("Password Changed SuccessFully");
		response.setStatus(3);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//----------------------Add Profile Image---------------------
	@RequestMapping(value = "/addImage",method = RequestMethod.PUT)
	private ResponseEntity<ResponseDto> uploadProfileImage(@RequestParam String token,@RequestParam MultipartFile file,HttpServletResponse res) throws TokenParsingException, UserNotFoundException, IOException{
		
		String imageUrl =  userService.addimage(token,file);
		
		res.setHeader("ImageUrl",imageUrl );
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage("Image Added");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//----------------------Remove Profile Image--------------------
	@RequestMapping(value="/removeImage",method = RequestMethod.PUT)
	private ResponseEntity<ResponseDto> removeProfileImage(@RequestParam String token,@RequestParam String fileName) throws TokenParsingException, UserNotFoundException{
		
		userService.removeImage(token,fileName);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage("Image Removed");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
