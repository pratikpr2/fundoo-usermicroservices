package com.bridgelabz.fundoonotes.user.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bridgelabz.fundoonotes.user.utility.Utility;

//@Service
public class AmazonS3Service implements ImageServices {

	private String accessKey = System.getenv("accessKey");

	private String secretKey = System.getenv("secretKey");

	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

	@Autowired
	AmazonS3 s3client;

	@Value("${bucketName}")
	String bucketName;

	private String folderName;


	@Override
	public String addImages(String userId, MultipartFile file) throws IOException {

		folderName = userId;
		List<Bucket> bucketList = s3client.listBuckets();
		if (bucketList.isEmpty()) {
			s3client.createBucket(bucketName);
		}
		for (int i = 0; i < bucketList.size(); i++) {
			if (!bucketList.get(i).getName().equals(bucketName)) {
				s3client.createBucket(bucketName);
			}
		}

		String fileName = folderName + "/profilepic/" + file.getOriginalFilename();

		File myfile = Utility.convert(file);
		
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, myfile).withCannedAcl(CannedAccessControlList.PublicRead));

		String imageURL = getImageUrl(fileName);

		return imageURL;

	}

	@Override
	public String deleteImages(String userId, String fileName) {

		List<S3ObjectSummary> fileList = s3client.listObjects(bucketName, folderName).getObjectSummaries();

		for (int i = 0; i < fileList.size(); i++) {
			if (fileList.get(i).getKey().equals(folderName + "/profilepic/" + fileName)) {
				s3client.deleteObject(bucketName, fileList.get(i).getKey());
			}
		}

		String file = folderName+"/profilepic/"+fileName;
		
		String imageURL = getImageUrl(file);
		return imageURL;
	}

	@Override
	public void viewImage(String userId, String noteId) {

	}

	public String getImageUrl(String filename) {
		
		List<S3ObjectSummary> fileList = s3client.listObjects(bucketName, folderName+"/profilepic").getObjectSummaries();

		String url = null;
		for (S3ObjectSummary file : fileList) {
			if ((filename).equals(file.getKey())) {
				url =((AmazonS3Client) s3client).getResourceUrl(bucketName, file.getKey());
			}
		}
		return url;
		
	}

}
