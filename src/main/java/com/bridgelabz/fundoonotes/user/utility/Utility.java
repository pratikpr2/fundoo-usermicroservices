package com.bridgelabz.fundoonotes.user.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.user.exception.ChangePassException;
import com.bridgelabz.fundoonotes.user.exception.LoginException;
import com.bridgelabz.fundoonotes.user.exception.MailException;
import com.bridgelabz.fundoonotes.user.exception.MalformedUUIDException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.model.ChangePassDTO;
import com.bridgelabz.fundoonotes.user.model.LoginDTO;
import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.bridgelabz.fundoonotes.user.model.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepository;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

public class Utility {
	
	@Autowired 
	UserRepository mongoRepo;
	
	private static final String CONTACT_PATTERN = "^[0-9]{10}$";
	private static final String EMAIL_PATTERN = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$";
	private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
	
	public static void validateRegUser(RegistrationDTO registrationDto) throws RegistrationException{
		
		if(registrationDto.getUserName()==null || registrationDto.getUserName().length()<3) {
			throw new RegistrationException("User Name should Have atleast 3 Characters");
		}
		else if(registrationDto.getEmailId().trim().length()==0) {
			throw new RegistrationException("Please Enter a Valid Email Id");
		}
		else if(registrationDto.getPhoneNumber()==null || !registrationDto.getPhoneNumber().matches(CONTACT_PATTERN)) {
			throw new RegistrationException("Contact Number Should be 10 digits Number");
		}
		else if(registrationDto.getPassword()==null || !registrationDto.getPassword().matches(PASSWORD_PATTERN)) {
			throw new RegistrationException("Password Should be of atleast 8 AphaNumeric Characters");
		}
	
		else if(registrationDto.getConfirmPassword()==null || !registrationDto.getConfirmPassword().equals(registrationDto.getPassword())) {
			throw new RegistrationException("Confirm Password Didn't Match With Password");
		}
		
	}
	
	public static void validateLoginUser(LoginDTO loginDto) throws LoginException {
		if(loginDto.getEmail().trim().length()==0 || loginDto.getEmail().length() <8) {
			throw new LoginException("Please Enter A valid Email Id");
		}
		else if(loginDto.getPassword().trim().length()==0 || loginDto.getPassword().length() < 8) {
			throw new LoginException("Password Should be of atleast 8 AphaNumeric Characters");
		}
	}
	
	public static void validateEmailDto(MailDTO maildto) throws MailException {
		if(maildto.getEmail()==null || !maildto.getEmail().matches(EMAIL_PATTERN)) {
			throw new MailException("Invalid Email");
		}
		else if(maildto.getBody()==null) {
			throw new MailException("Mail Body Cannot be Null");
		}
		else if(maildto.getSubject()==null) {
			throw new MailException("Mail Subject Cannot be Null");
		}
	}
	public static void validateChangePassDto(ChangePassDTO changepass) throws ChangePassException {
		if(changepass.getPassword()==null || changepass.getPassword().length() <8) {
			throw new ChangePassException("Invalid Password");
		}
		else if(!changepass.getPassword().equals(changepass.getConfirmPassword())) {
			throw new ChangePassException("Confirm Password Didn't Match with Password");
		}
	}
	
	public static String generateToken(User user) {
		JwtToken jwt = new JwtToken();
		String token = jwt.createJWT(user);
		return token;
	}
	
	public static String generate() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static void validateUUID(String UUID) throws MalformedUUIDException {
	
		if(UUID==null) {
			throw new MalformedUUIDException("Malformed UUID");
		}
	}
	
	public static File convert(MultipartFile file) throws IOException
	{    
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}
}
