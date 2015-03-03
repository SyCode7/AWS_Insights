package com.amazonaws.tests;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.ObjectMetadataProvider;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class MultiPartUpload {
	
	static String existingBucketName = "testing020315";
	static String keyName = "uploading";
	static String virtualDirectoryKeyPrefix = "uploadDirectory";
	static List fileList [] = new List[3];
	String file1 = "C:\\Users\\Kennedy\\testfiles\\AWS_SDK4Java.pdf";
	String file2 = "C:\\Users\\Kennedy\\testfiles\\LogInfo.out";
	String file3 = "C:\\Users\\Kennedy\\testfiles\\test.txt";
	String file4 = "C:\\Users\\Kennedy\\testfiles\\url-list.txt";
//	DirectoryStream dir = "C:\\Users\\Kennedy\\testfiles";
	final String key = "test";
	final String value = "value";
	
	
	TransferManager tm = new TransferManager (new ProfileCredentialsProvider());
//	System.out.println("transfer");
	
	
	static ObjectMetadataProvider  metadataProvider = new ObjectMetadataProvider(){
		
		public void provideObjectMetadata(File file1, ObjectMetadata metadata) {
			
//				String file = "C:\\Users\\Kennedy\\testfiles\\AWS_SDK4Java.pdf";
			if (isPDF (file1)) {
				metadata.addUserMetadata(key, value);
			}
		}
		
	
	
	public static void main(String[] args) throws Exception {
			
			
		MultipleFileUpload upload = tm.uploadFileList(existingBucketName, virtualDirectoryKeyPrefix, fileList, metadataProvider );
		try {
			upload.waitForCompletion();
			System.out.println("file upload is successfully done !!!");
		} catch (AmazonClientException amazonClientException) {
			System.out.println("The upload was unfortunatley unsuccessful, sequence aborted");
			amazonClientException.printStackTrace();
		}
		
		
	}
	private boolean isPDF(File file1) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
