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
        S3Object object = s3client.getObject(BUCKET_NAME, "analysis/binaries/"+key);
        InputStream is = object.getObjectContent();

        return is;
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Hello world!");
        String path = "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29" +
                "/wat/CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.wat.gz";

        InputStream is = loadSpreadsheet(path);
        WATParser.parse(path, is);
    }
}
