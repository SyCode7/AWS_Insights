/*
 * Copyright 2010-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketLoggingConfiguration;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class S3Connection {
	
	
	static Logger log = Logger.getLogger(S3Connection.class.getName());
	static Date date = new Date ();
	static File file = new File("C:\\Users\\Kennedy\\testfiles\\LogInfo");
	String log4jConfPath = "/path/to/log4j.properties";
	PropertyConfigurator.configure(log4jConfPath);
	
	
	
    public static void main(String[] args) throws IOException {
        
    	AWSCredentials credentials = null;
    	try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. " +
					 "Please make sure that your credentials file is at the correct " +
					 "location (~/.aws/credentials), and is in valid format.", e);
			
			 
		}
    	
		
        AmazonS3 s3 = new AmazonS3Client();
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);

        String bucketName = "testing020315";
        String key = "loggingFile";


        try {

//            System.out.println("Creating bucket " + bucketName  +  "\n");
//            s3.createBucket(bucketName);
//            System.out.println(bucketName + "has been created successfully !!!");
//
//            /*
//             * List the buckets in your account
//             */
            System.out.println("Listing objects in bucket :" + bucketName);
            for (Bucket bucket : s3.listBuckets()) {
                System.out.println(" - " + bucket.getName());
            }
            System.out.println();

           
            System.out.println("Uploading a new object to S3 from a file\n");
            s3.putObject(bucketName, key, file);
            BucketLoggingConfiguration loggingINfo = s3.getBucketLoggingConfiguration(bucketName);
            System.out.println("getBucketLoggingConfiguration is" + loggingINfo ) ;
            
//        	PutObjectRequest uploadFile = new PutObjectRequest (bucketName, key, file);


           
//            s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));   
            log.trace("Success, this is an debug message at " + date );

            System.out.println("Downloading an object");
            s3.getObject(bucketName, key);
//            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
//            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
//            displayTextInputStream(object.getObjectContent());

           
            System.out.println("Listing objects");
            ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix("My"));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " +
                        "(size = " + objectSummary.getSize() + ")");
            }
            System.out.println();

             /*
             * Delete a bucket - A bucket must be completely empty before it can be
             * deleted, so remember to delete any objects from your buckets before
             * you try to delete them.
             */
//            System.out.println("Deleting bucket " + bucketName + "\n");
//            s3.deleteBucket(bucketName);
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    /**
     * Creates a temporary file with text data to demonstrate uploading a file
     * to Amazon S3
     *
     * @return A newly created temporary file with text data.
     *
     * @throws IOException
     */
//    private static File createSampleFile() throws IOException {
//        File file = File.createTempFile("aws-java-sdk-", ".txt");
////        file.deleteOnExit();
//
//        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
//        writer.write("this is a test transmission of\n");
//        writer.write("my signed URL generation request\n");
//        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
//        writer.write("01234567890112345678901234\n");
//        writer.write("abcdefghijklmnopqrstuvwxyz\n");
//        writer.close();
//
//        return file;
//    }

    /**
     * Displays the contents of the specified input stream as text.
     *
     * @param input
     *            The input stream to display as text.
     *
     * @throws IOException
     */
    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

}
