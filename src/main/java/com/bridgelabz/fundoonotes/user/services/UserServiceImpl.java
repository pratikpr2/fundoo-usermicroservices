package com.bridgelabz.fundoonotes.user.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.bridgelabz.fundoonotes.user.model.MailUser;
import com.bridgelabz.fundoonotes.user.model.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.rabbitmq.ProducerService;
import com.bridgelabz.fundoonotes.user.repository.TokenRepository;
import com.bridgelabz.fundoonotes.user.repository.UserHighLevelElasticRepo;
import com.bridgelabz.fundoonotes.user.repository.UserRepository;
import com.bridgelabz.fundoonotes.user.token.JwtToken;
import com.bridgelabz.fundoonotes.user.utility.Utility;

@Service
public class UserServiceImpl implements UserService {

	@Value("${loginLink}")
	public String link;
	
	@Value("${activationLink}")
	public String activatelink;
	
	@Value("${resetPasswordLink}")
	public String resetPassLink;
	
	@Autowired 
	UserRepository mongoRepo;
	
	@Autowired
	UserMailService mailservice;
	
	@Autowired
	UserHighLevelElasticRepo highElasticRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	TokenRepository tokenRepo;
	
	@Autowired
	ProducerService producer;
	
	@Autowired
	JwtToken jwt;
	
	/*@Autowired
	ImageServices imageService;*/
	
	/*@Autowired
	MessageService sqsService;*/
	
	@Override
	public String login(LoginDTO logUser) throws LoginException, MessagingException {

		Utility.validateLoginUser(logUser);
		
		Optional<User> user = highElasticRepo.findByUserEmail(logUser.getEmail());
		
		if(user==null) {
			throw new LoginException("User With Email "+logUser.getEmail()+" Not Registered");
		}
		else if(!user.get().isStatus()) {
			throw new LoginException("Please Activate Account");
			
		}
		else if(!passwordEncoder.matches(logUser.getPassword(), user.get().getPassword())) {
			throw new LoginException("Wrong Password");
		}
		
		String token =jwt.createJWT(user.get());
		
		String loginLink = link+token;
		
		System.out.println(token);
		
		MailDTO mailuser = new MailDTO();
		mailuser.setEmail(user.get().getUserEmail());
		mailuser.setSubject("Login Validation");
		mailuser.setBody(loginLink);
		
		producer.send(mailuser);
		
		//sqsService.sendMessage(mailuser);
		return token;
	}

	@Override
	public String register(RegistrationDTO regUser) throws RegistrationException ,MessagingException{
		
		Utility.validateRegUser(regUser);
		
		Optional<User> checkuser= null;
				
		checkuser=highElasticRepo.findByUserEmail(regUser.getEmailId());
		
		if(checkuser!=null) {
			throw new RegistrationException("User with Email "+ regUser.getEmailId()+" already Exists");
		}
		
		User user = new User();
		user.setUserName(regUser.getUserName());
		user.setUserEmail(regUser.getEmailId());
		user.setPhoneNumber(regUser.getPhoneNumber());
		user.setPassword(passwordEncoder.encode(regUser.getPassword()));
		
		mongoRepo.save(user);
		highElasticRepo.save(user);
		
		String currentJwt = jwt.createJWT(user);
		
		String activationLink = activatelink+currentJwt;
		
		MailDTO mailuser = new MailDTO();
		mailuser.setEmail(user.getUserEmail());
		mailuser.setSubject("Activate Account");
		mailuser.setBody(activationLink);
		
		producer.send(mailuser);
		
		return currentJwt;
		
	}

	@Override
	public void activateUser(String token) throws ActivationException, TokenParsingException {
		
		Optional<User> user =  highElasticRepo.findById(jwt.getUserId(token));
		
		if(user==null) {
			throw new ActivationException("Account Activation failed");
		}
		
		user.get().setStatus(true);
		
		mongoRepo.save(user.get());
		highElasticRepo.save(user.get());
		
	}

	@Override
	public void changePassword(ChangePassDTO reset,String UUID) throws ChangePassException, MessagingException, ActivationException, TokenParsingException, MalformedUUIDException {
		
		Utility.validateChangePassDto(reset);
		Utility.validateUUID(UUID);
		
		String userId = tokenRepo.find(UUID);
		
		Optional<User> user = highElasticRepo.findById(userId);
		
		if(user==null) {
			throw new ActivationException("Invalid User");
		}
		
		user.get().setPassword(passwordEncoder.encode(reset.getPassword()));
		
		mongoRepo.save(user.get());
		highElasticRepo.save(user.get());
		tokenRepo.delete(UUID);
	}

	@Override
	public void forgetPassword(MailUser mail) throws MessagingException, ChangePassException {
		
		Optional<User> checkUser = highElasticRepo.findByUserEmail(mail.getEmail());

		if(checkUser==null) {
			throw new ChangePassException("User Is Not Registered");
		}
		
		String UUID = Utility.generate();
		String mailBody = resetPassLink +UUID;
		
		MailDTO usermail = new MailDTO();
		usermail.setEmail(mail.getEmail());
		usermail.setSubject("Password Reset");
		usermail.setBody(mailBody);
		
		tokenRepo.save(UUID,checkUser.get().getUserId());
		producer.send(usermail);
	
	}

	@Override
	public String addimage(String token, MultipartFile file) throws TokenParsingException, UserNotFoundException, IOException {
		
		/*Optional<User> user = highElasticRepo.findById(jwt.getUserId(token));
		
		if(user==null) {
			throw new UserNotFoundException("Invalid User");
		}
		
		String imageURl =imageService.addImages(jwt.getUserId(token), file);
		
		List<String> imageUrlList = user.get().getImageLink();
		
		imageUrlList.add(imageURl);
		user.get().setImageLink(imageUrlList);
		user.get().setProfileImageLink(imageURl);
		
		mongoRepo.save(user.get());
		highElasticRepo.save(user.get());
		
		return imageURl;*/
		
		return null;
		
	}

	@Override
	public void removeImage(String token,String filename) throws TokenParsingException, UserNotFoundException {
		/*
		Optional<User> user = highElasticRepo.findById(jwt.getUserId(token));
		
		if(user==null) {
			throw new UserNotFoundException("Invalid User");
		}
		
		String imageUrl =imageService.deleteImages(user.get().getUserId(), filename);
		
		List<String> imageUrlList = user.get().getImageLink();
		
		imageUrlList.remove(imageUrl);
		
		if(user.get().getProfileImageLink().equals(imageUrl)){
			user.get().setProfileImageLink(null);
		}
		
		user.get().setImageLink(imageUrlList);
		
		mongoRepo.save(user.get());
		highElasticRepo.save(user.get());*/
	}
	

}
