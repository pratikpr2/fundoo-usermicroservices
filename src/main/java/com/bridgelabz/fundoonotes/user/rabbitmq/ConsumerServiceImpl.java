package com.bridgelabz.fundoonotes.user.rabbitmq;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.bridgelabz.fundoonotes.user.services.UserMailService;

@Service
public class ConsumerServiceImpl implements ConsumerService {

	/*@Autowired
	AmazonSQS sqs;*/

	@Autowired
	UserMailService usermail;

	/*ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
			"https://sqs.us-east-1.amazonaws.com/639670040330/myqueue").withWaitTimeSeconds(10)
					.withMaxNumberOfMessages(10);


	MailDTO mail = new MailDTO();

	@Override
	public void receive(MailDTO mail) throws MessagingException {
		
		List<Message> sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();

		
		mail.setBody(sqsMessages.get(0).toString());
		mail.setSubject("Logged In");
		mail.setEmail(mail.getEmail());
		usermail.sendMailv2(mail);

	}*/

	 @RabbitListener(queues="javainuse.queue") public void receive(MailDTO mail)
	 throws MessagingException { System.out.println("Receive msg = "+mail);
	 usermail.sendMailv2(mail); }
	 

}
