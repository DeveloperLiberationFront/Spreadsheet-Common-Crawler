package net.barik.spreadsheet;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;

import net.barik.spreadsheet.analysis.AnalysisOutput;
import net.barik.spreadsheet.analysis.SpreadsheetAnalyzer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AnalysisMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    	Configuration conf = context.getConfiguration();

        String importBucket = conf.get("import.bucket", "barik-cc");
        String importKeyPrefix = conf.get("import.keyprefix", "analysis/binaries/");
 
        String exportBucket = conf.get("export.bucket", "barik-cc");
        String exportKeyPrefix = conf.get("export.keyprefix", "analysis/output/");
    	
    	
    	String path = importKeyPrefix + value.toString();

        InputStream is = S3Load.loadSpreadsheet(importBucket, path);
        
        AnalysisOutput ao = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, path);

        JacksonS3Export.exportItem(ao, exportBucket, exportKeyPrefix, value.toString());
        is.close();

        context.write(new Text(path), new Text(ao.errorNotification));

    }
}
