package com.amazonaws.tests;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class SignedURLS {
	
	private static String bucketName = "testwithobject";
	private static String objectKey = "newlevels";
	
	public static void main(String[] args) {
		AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		try {
			System.out.println("generating pre-signed URLs");
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000*60*60;
			expiration.setTime(100000000);
			
			GeneratePresignedUrlRequest  generatePresignedUrlRequest = new GeneratePresignedUrlRequest (bucketName, objectKey);
			generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
			generatePresignedUrlRequest.setExpiration(expiration);
			
			URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
			UploadObject(url);
			System.out.println("PreSigned URL = " + url.toString());
		} catch (AmazonServiceException exception) {
			System.out.println("caught an AmazonServiceException, " + "which means that your request made it to Amazon but was rejected with an error response " + "for some reason");
			System.out.println("Error message: " + exception.getMessage() );
			System.out.println("HTTP Error Code: " + exception.getStatusCode());
			System.out.println("AWS Error Code: " + exception.getErrorCode());
			System.out.println("Error type:" + exception.getErrorType());
			System.out.println("Request ID: " + exception.getRequestId());
		}   catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException" + "which means tht the client encountered an internal error"
					+ " while  trying to communicate" + "with S3," + "such as not being able to access the network.");
			System.out.println("Error Message:"+ ace.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void UploadObject(URL url) throws IOException {
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		OutputStreamWriter out = new OutputStreamWriter( connection.getOutputStream());
		out.write("the text uploaded as object");
		out.close();
		int responseCode = connection.getResponseCode();
		System.out.println("Service returned response code " + responseCode);
		
		
	}

}
