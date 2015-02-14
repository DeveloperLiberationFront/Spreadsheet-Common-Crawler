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

    public static void exportItems(List<WATExportModel> items) throws IOException {
        for (WATExportModel watExportModel : items) {
            byte[] data = serializeModel(watExportModel);
            putObjectS3(BUCKET_NAME, getKeyForURI(watExportModel.getWarcRecordId()), data);
        }
    }
    
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

    public static void main(String[] args) throws IOException {
        WATExportModel watExportModel = new WATExportModel(
                "common-crawl/crawl-data/CC-MAIN-2014-35/segments/" +
                        "1408500800168.29/wat/CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.wat.gz",
                "http://digitalcommons.uconn.edu/cgi/" +
                        "viewcontent.cgi?filename=2&article=1001&context=uchcedu_annreports&type=additional",
                "2014-08-20T04:53:54Z",
                "<urn:uuid:639a339b-40aa-4aca-a629-186c9adbe2a2>",
                "<urn:uuid:41a7feb1-46aa-4d90-acb7-205851f567a3>",
                "application/json",
                1858,
                "{ \"Envelope\": {} }"
        );

        byte[] data = serializeModel(watExportModel);
        putObjectS3(BUCKET_NAME, getKeyForURI(watExportModel.getWarcRecordId()), data);
    }
}
