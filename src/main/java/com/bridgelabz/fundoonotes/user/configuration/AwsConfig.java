package com.bridgelabz.fundoonotes.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

//@Configuration
public class AwsConfig {

	private String accessKey = System.getenv("accessKey");

	private String secretKey = System.getenv("secretKey");

	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

	AmazonS3 s3client;
	

	//@Bean
	public AmazonS3 getS3Client() {

		AWSCredentials awscredentials = null;
		try {
			awscredentials = new AWSCredentials() {

				@Override
				public String getAWSSecretKey() {
					return secretKey;

				}

				@Override
				public String getAWSAccessKeyId() {
					return accessKey;
				}
			};
		} catch (Exception exception) {
			throw new AmazonClientException("Invalid Aws Credentials", exception);
		}
		s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awscredentials))
				.withRegion("us-east-1").build();

		return s3client;
	}

	/*@Bean
	public AmazonSQS getsqsClient() {
		AmazonSQS sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1).build();
		return sqs;
	}*/

}
