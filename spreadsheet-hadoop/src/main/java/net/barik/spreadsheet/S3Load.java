package net.barik.spreadsheet;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import java.io.InputStream;

public class S3Load {
    public static final String BUCKET_NAME = "barik-cc";

    public static InputStream loadSpreadsheet(String key) throws IOException {
        AmazonS3 s3client = new AmazonS3Client();
        S3Object object = s3client.getObject(BUCKET_NAME, key);
        InputStream is = object.getObjectContent();

        return is;
    }

}
