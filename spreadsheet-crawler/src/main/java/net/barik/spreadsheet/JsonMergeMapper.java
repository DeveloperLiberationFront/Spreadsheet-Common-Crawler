package net.barik.spreadsheet;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import com.amazonaws.AmazonClientException;

public class JsonMergeMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException {
        String resourceKey = value.toString();
        Configuration conf = context.getConfiguration();

        // Type can be s3 or local; s3 by default.
        String importType = conf.get("import.type", "s3");
        String exportType = conf.get("export.type", "s3");

        // Used when type is local.
        String importPathSource = conf.get("import.path.source", "./");
        String importPathDest = conf.get("import.dest.source", "./");
        String exportPath = conf.get("export.path", "./");

        // Used when type is s3.
        String importBucket = conf.get("import.bucket", "barik-cc");

        String importKeyPrefixSource = conf.get("import.keyprefix.source", "cc-warc-stage4/");
        String importKeyPrefixDest = conf.get("import.keyprefix.dest", "cc-warc-stage4/");

        String exportBucket = conf.get("export.bucket", "barik-cc");
        String exportKeyPrefix = conf.get("export.keyprefix", "cc-warc-stage4-filter/");

        String tmpDirectory = conf.get("tmp.directory", "/mnt/");

        RecordIO importSourceIO;
        RecordIO importDestIO;

        if (importType.equals("s3")) {
            importSourceIO = new S3RecordIO(importBucket, importKeyPrefixSource, tmpDirectory);
            importDestIO = new S3RecordIO(importBucket, importKeyPrefixDest, tmpDirectory);

        } else {
            importSourceIO = new LocalRecordIO(importPathSource);
            importDestIO = new LocalRecordIO(importPathDest);
        }

        RecordIO exportIO;
        if (exportType.equals("s3")) {
            exportIO = new S3RecordIO(exportBucket, exportKeyPrefix, tmpDirectory);
        } else {
            exportIO = new LocalRecordIO(exportPath);
        }

        JsonMergeModel model = new JsonMergeModel(exportIO);

        while (true) {
            try {
                String source = IOUtils.toString(importSourceIO.loadIntoMemory(resourceKey), "UTF-8");
                String dest = IOUtils.toString(importDestIO.loadIntoMemory(resourceKey), "UTF-8");

                JSONObject jsonSource = new JSONObject(source);
                JSONObject jsonDest = new JSONObject(dest);

                JSONObject result = model.merge(jsonSource, jsonDest, "Tika");
                model.export(resourceKey, result);

            }
            catch (AmazonClientException e) {
                e.printStackTrace();
                continue;
            }
            catch (SocketTimeoutException e) {
                e.printStackTrace();
                continue;
            }
            catch (SocketException e) {
                e.printStackTrace();
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

            break;
        }
    }
}
