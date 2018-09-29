package com.bridgelabz.fundoonotes.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

//@Configuration
public class SQSConfig {

	/*private String accessKey = System.getenv("accessKey");

	private String secretKey = System.getenv("secretKey");

	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

	AmazonSQS sqs;

	@Bean
	public AmazonSQS createSQSClient() {
		sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1).build();
		return sqs;
	}*/

}
