package net.barik.spreadsheet;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.*;
import java.util.zip.ZipException;

public class WARCExportMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();

        // Type can be s3 or local; s3 by default, and unrecognized is considered local.
        String exportType = conf.get("export.type", "s3");
        String importType = conf.get("import.type", "s3");

        // Used when type is local.
        String exportPath = conf.get("export.path", "./");
        String importPath = conf.get("import.path", "./");

        // Used when type is s3.
        String importBucket = conf.get("import.bucket", "barik-cc");
        String importKeyPrefix = conf.get("import.keyprefix", "cc-wat-stage1/");

        String exportBucket = conf.get("export.bucket", "barik-cc");
        String exportKeyPrefix = conf.get("export.keyprefix", "cc-warc-stage2/");

        String tmpDirectory = conf.get("tmp.directory", "/mnt/");

        // Source of WAT record.
        RecordIO mapSourceIO;

        if (importType.equals("s3")) {
            mapSourceIO = new S3RecordIO(importBucket, importKeyPrefix, tmpDirectory);
        }
        else {
            mapSourceIO = new LocalRecordIO(importPath);
        }

        InputStream is = mapSourceIO.load(value.toString());
        JSONObject json = loadWATAsJSON(is);

        // Set corresponding WARC record export location.
        RecordIO exportIO;
        if (exportType.equals("s3")) {
            exportIO = new S3RecordIO(exportBucket, exportKeyPrefix, tmpDirectory);
        }
        else {
            exportIO = new LocalRecordIO(exportPath);
        }

        RecordIO importIO = new S3RecordIO(S3RecordIO.AWS_PUBLIC_BUCKET);

        try {
            WARCExportModel model = new WARCExportModel(json, importIO, exportIO);

            while (true) {
                try {
                    model.process();
                }
                catch (AmazonClientException e) {
                    e.printStackTrace();
                    continue;
                }

                break;
            }

            // Let the reducer know we've processed this one.
            context.write(value, new Text(model.getTargetResourceKey()));
        }
        catch (ZipException e) {
            // This WARC file is corrupted.
            e.printStackTrace();
        }
    }

    public static JSONObject loadWATAsJSON(InputStream inputStream) throws IOException {
        String watRecord = IOUtils.toString(inputStream);
        JSONObject json = new JSONObject(watRecord);
        return json;
    }
}
