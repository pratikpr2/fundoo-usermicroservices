package com.bridgelabz.fundoonotes.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

//@Configuration
public class JMSSQSConfig {

	String queueName= "myqueue";
	
	private String accessKey = System.getenv("accessKey");

	private String secretKey = System.getenv("secretKey");
	
	AWSCredentials credentials1 = new BasicAWSCredentials(accessKey, secretKey);

	AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(credentials1);
	
	//@Autowired
	private SQSListener sqsListener;

	//@Bean
	public DefaultMessageListenerContainer jmsListenerContainer() {
		 SQSConnectionFactory sqsConnectionFactory = new SQSConnectionFactory(
	                new ProviderConfiguration(),
	                AmazonSQSClientBuilder.standard()
	                        .withRegion(Regions.US_EAST_1)
	                        .withCredentials(credentials)
	                );
		DefaultMessageListenerContainer dmlc = new DefaultMessageListenerContainer();
		dmlc.setConnectionFactory(sqsConnectionFactory);
		dmlc.setDestinationName(queueName);
		dmlc.setMessageListener(sqsListener);
		return dmlc;
	}

//	@Bean
	public JmsTemplate createJMSTemplate() {
		SQSConnectionFactory sqsConnectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1)
                        .withCredentials((AWSCredentialsProvider) credentials)
                );
		JmsTemplate jmsTemplate = new JmsTemplate(sqsConnectionFactory);
		jmsTemplate.setDefaultDestinationName(queueName);
		jmsTemplate.setDeliveryPersistent(false);
		return jmsTemplate;
	}


}
