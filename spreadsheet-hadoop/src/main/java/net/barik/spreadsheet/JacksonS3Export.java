package net.barik.spreadsheet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import net.barik.AnalysisOutput;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonS3Export {
    public static final String BUCKET_NAME = "barik-cc";
    
    public static void exportItem(AnalysisOutput ao) throws IOException {
            byte[] data = serializeModel(ao);
            putObjectS3(BUCKET_NAME, "analysis/output/" + ao.fileName+".json" , data);

    }

	public static String getKeyForURI(String uri) {
        return uri.substring(10, uri.length() - 1);
    }

    public static byte[] serializeModel(Object model) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        mapper.writeValue(os, model);
        byte[] data = os.toByteArray();

        return data;
    }

    public static void putObjectS3(String bucket, String key, byte[] data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(data.length);

        AmazonS3 s3client = new AmazonS3Client();
        s3client.putObject(bucket, key, is, objectMetadata);
    }

}
