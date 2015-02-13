package net.barik.spreadsheet;

import net.barik.AnalysisOutput;
import net.barik.SpreadsheetAnalyzer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AnalysisMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String path = value.toString();

        InputStream is = S3Load.loadSpreadsheet(path);
        
        AnalysisOutput ao = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, path);
        
        JacksonS3Export.exportItem(ao);
        is.close();

        context.write(new Text(path), new Text(ao.errorNotification));

    }
}
