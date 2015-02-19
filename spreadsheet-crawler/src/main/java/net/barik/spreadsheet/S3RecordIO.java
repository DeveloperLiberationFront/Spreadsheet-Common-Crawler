package net.barik.spreadsheet;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveRecord;
import org.archive.streamcontext.Stream;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class S3RecordIO implements RecordIO {
    public static final String AWS_PUBLIC_BUCKET = "aws-publicdatasets";

    private String bucketName;
    private String keyPrefix;
    private String tmpDirectory;

    public S3RecordIO(String bucketName) {
        this(bucketName, "", "");
    }

    public S3RecordIO(String bucketName, String keyPrefix, String tmpDirectory) {
        this.bucketName = bucketName;
        this.keyPrefix = keyPrefix;
        this.tmpDirectory = tmpDirectory;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    @Override
    public File loadIntoFile(String resourceKey) throws IOException {
        AmazonS3 s3client = new AmazonS3Client();
        S3Object object = s3client.getObject(bucketName, keyPrefix + resourceKey);
        S3ObjectInputStream inputStream = object.getObjectContent();

        String fileName = DigestUtils.shaHex(resourceKey);

        File file = new File(tmpDirectory, fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        IOUtils.copy(inputStream, fileOutputStream);
        fileOutputStream.close();

        return file;
    }

    @Override
    public InputStream loadIntoMemory(String resourceKey) throws IOException {
        AmazonS3 s3client = new AmazonS3Client();
        S3Object object = s3client.getObject(bucketName, keyPrefix + resourceKey);

        int bytes = (int)object.getObjectMetadata().getContentLength();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int)bytes);

        S3ObjectInputStream objectContent = object.getObjectContent();
        IOUtils.copy(objectContent, byteArrayOutputStream);

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public InputStream load(String resourceKey) {
        AmazonS3 s3client = new AmazonS3Client();
        S3Object object = s3client.getObject(bucketName, keyPrefix + resourceKey);
        return object.getObjectContent();
    }

    @Override
    public InputStream load(String resourceKey, long start, long end) throws IOException {
        AmazonS3 s3client = new AmazonS3Client();

        GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucketName, keyPrefix + resourceKey);
        rangeObjectRequest.setRange(start, end);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        S3Object objectPortion = s3client.getObject(rangeObjectRequest);
        IOUtils.copy(objectPortion.getObjectContent(), byteArrayOutputStream);

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public void save(String resourceKey, byte[] data) {
        AmazonS3 s3client = new AmazonS3Client();

        ByteArrayInputStream is = new ByteArrayInputStream(data);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(data.length);
        s3client.putObject(bucketName, keyPrefix + resourceKey, is, objectMetadata);
    }

    @Override
    public void save(String resourceKey, ArchiveRecord record) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            record.dump(byteArrayOutputStream);
            save(resourceKey, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
