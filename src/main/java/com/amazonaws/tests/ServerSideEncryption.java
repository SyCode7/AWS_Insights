package com.amazonaws.tests;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.transfer.TransferManager;

public class ServerSideEncryption {
	
	private static String existingBucketName = "testwithobject";
	private static String keyName = "newlevels";
	private static String fileToUpload = "C:\\Users\\Kennedy.Torkura\\Documents\\TEST";
	private static String keyname = "newlevels";
	private static String targetKeyName = "newlevels";


	
	TransferManager tr =  new TransferManager(new ProfileCredentialsProvider());
	

}
