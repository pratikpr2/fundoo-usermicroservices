package com.bridgelabz.fundoonotes.user.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

public interface ImageServices {

	//public AmazonS3 getS3Client();
	String addImages(String userId,MultipartFile file) throws IOException;
	String deleteImages(String userId,String fileName);
	void viewImage(String userId,String noteId);
	
}
