package net.barik.spreadsheet;

import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

public class S3Load {
    public static final String BUCKET_NAME = "barik-cc";

    public static InputStream loadSpreadsheet(String qualifiedS3Path) throws IOException {
        AmazonS3 s3client = new AmazonS3Client();
        System.out.println("Reading from "+qualifiedS3Path +" in bucket "+BUCKET_NAME);
        S3Object object = s3client.getObject(BUCKET_NAME, qualifiedS3Path);
        InputStream is = object.getObjectContent();

        return is;
    }

}
