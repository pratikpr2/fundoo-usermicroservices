package com.bridgelabz.fundoonotes.user.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.bridgelabz.fundoonotes.user.model.MailDTO;

@Service
public class ProducerServiceImpl implements ProducerService {

	/*
	 * @Autowired AmazonSQS sqs;
	 * 
	 * @Override public void send(MailDTO mail) { Map<String, MessageAttributeValue>
	 * messageAttributes = new HashMap<>(); messageAttributes.put("AttributeOne",
	 * new
	 * MessageAttributeValue().withStringValue("This is an Attribute").withDataType(
	 * "String"));
	 * 
	 * SendMessageRequest sendMessageStandardQueue = new
	 * SendMessageRequest().withQueueUrl(
	 * "https://sqs.us-east-1.amazonaws.com/639670040330/myqueue")
	 * .withMessageBody(mail.getBody()).withDelaySeconds(30).withMessageAttributes(
	 * messageAttributes);
	 * 
	 * sqs.sendMessage(sendMessageStandardQueue);
	 * 
	 * 
	 * }
	 */

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${javainuse.rabbitmq.exchange}")
	private String exchange;

	@Value("${javainuse.rabbitmq.routingkey}")
	private String routingkey;

	@Override
	public void send(MailDTO mail) { 
		rabbitTemplate.convertAndSend(exchange, routingkey, mail);
		System.out.println("Send msg = " + mail);

	}

}
