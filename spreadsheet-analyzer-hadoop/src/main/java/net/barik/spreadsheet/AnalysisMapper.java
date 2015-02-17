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
    	
    	String corpusName = conf.get("corpus.name", "[unspecified]");

        String importBucket = conf.get("import.bucket", "barik-cc");
        String importKeyPrefix = conf.get("import.keyprefix", "analysis/binaries/");
 
        String exportBucket = conf.get("export.bucket", "barik-cc");
        String exportKeyPrefix = conf.get("export.keyprefix", "analysis/output/");
    	
    	
    	String fileName = value.toString();
		String path = importKeyPrefix + fileName;

        InputStream is = S3Load.loadSpreadsheet(importBucket, path);
        
        AnalysisOutput ao = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, corpusName, fileName);

        JacksonS3Export.exportItem(ao, exportBucket, exportKeyPrefix, fileName);
        is.close();

        context.write(new Text(path), new Text(ao.errorNotification));

    }
}
