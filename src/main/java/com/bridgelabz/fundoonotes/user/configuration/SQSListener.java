package com.bridgelabz.fundoonotes.user.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.bridgelabz.fundoonotes.user.services.UserMailService;
import com.google.gson.Gson;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.mail.MessagingException;

//@Component
public class SQSListener{
	
//public class SQSListener implements MessageListener {

	/*@Autowired
	UserMailService sendmail;

	private static final Logger LOGGER = LoggerFactory.getLogger(SQSListener.class);

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		
		String gsonText="";
		
		try {
			gsonText = textMessage.getText();
			
			LOGGER.info("Received message " + gsonText);
		} catch (JMSException e) {
			LOGGER.error("Error processing message ", e);
		}
		
		Gson gson = new Gson();
		MailDTO mail =  gson.fromJson(gsonText, MailDTO.class);
		
		try {
			sendmail.sendMailv2(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}*/

}
