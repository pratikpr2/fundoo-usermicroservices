package com.bridgelabz.fundoonotes.user.services;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.google.gson.Gson;

//@Service
public class MessageService {

	/*@Autowired
	private JmsTemplate jmsTemplate;
	
	private String queueName="myqueue";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

	public void sendMessage(final MailDTO message) {
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Gson gson = new Gson();
				
				String msg = gson.toJson(message);
				
				return session.createTextMessage(msg);
			}
		});
	}*/

}
