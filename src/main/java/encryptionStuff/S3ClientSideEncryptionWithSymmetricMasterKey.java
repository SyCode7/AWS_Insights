package encryptionStuff;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.junit.Assert;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.VersionListing;

public class S3ClientSideEncryptionWithSymmetricMasterKey {
	
	private static final String  masterKeyDir = "C:\\Users\\Kennedy\\testfiles";
	private static final String bucketName = UUID.randomUUID() + "-"
	+ DateTimeFormat.forPattern("yyMMdd-hhmmss").print(new DateTime());
	private static final String objectKey = UUID.randomUUID().toString();
	
	public static void main(String[] args) throws Exception{
		SecretKey mySymmetricKey = GenerateSymmetricMasterKey.loadSymmetricAESKey(masterKeyDir, "AES");
		EncryptionMaterials encryptionMaterials = new EncryptionMaterials(mySymmetricKey);
		AmazonS3EncryptionClient  encryptionClient = new AmazonS3EncryptionClient(new ProfileCredentialsProvider(), 
				new StaticEncryptionMaterialsProvider(encryptionMaterials));
		
		encryptionClient.createBucket(bucketName);
		System.out.println("COngratulations, your AWS bucket: " + bucketName + "has been succesfully created");
		
		byte [] plaintext =  "Hello world, S3 client-side encryption using assymmetric master key !".getBytes();
		System.out.println("plaintext's length: " + plaintext.length);
		encryptionClient.putObject(new PutObjectRequest(bucketName, objectKey, new ByteArrayInputStream(plaintext), new ObjectMetadata()));
		
		S3Object downloadedObject = encryptionClient.getObject(bucketName, objectKey);
		byte [] decrypted = IOUtils.toByteArray(downloadedObject.getObjectContent());
		
		Assert.assertTrue(Arrays.equals(plaintext, decrypted));
		deleteBucketAndAllContents(encryptionClient);
		
	}

	private static void deleteBucketAndAllContents(	AmazonS3 client) {
		System.out.println("Deleting S3 bucket" + bucketName);
		ObjectListing objectListing = client.listObjects(bucketName);
		
		while (true) {
			for (Iterator <?> iterator = objectListing.getObjectSummaries().iterator(); iterator.hasNext();) {
				S3ObjectSummary objectSummary = (S3ObjectSummary) iterator.next();
				client.deleteObject(bucketName, objectSummary.getKey());
				}
			if (objectListing.isTruncated()) {
				objectListing = client.listNextBatchOfObjects(objectListing);
								
			} else {
				break;
			}
			
		};
		
		VersionListing list = client.listVersions(new ListVersionsRequest().withBucketName(bucketName));
		for (Iterator <?> iterator = list.getVersionSummaries().iterator(); iterator.hasNext();){
			S3VersionSummary s = (S3VersionSummary) iterator.next();
			client.deleteVersion(bucketName, s.getKey(), s.getVersionId());
			}
		
		client.deleteBucket(bucketName);
		
	}

}
