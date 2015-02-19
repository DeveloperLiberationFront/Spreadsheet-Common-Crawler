package net.barik.spreadsheet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;

import net.barik.spreadsheet.analysis.AnalysisOutput;
import net.barik.spreadsheet.analysis.AnalysisOutputAndFormulas;
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
        
        String skipUniqueFormulas = conf.get("skip.formulas", "true"); //skip by default
    	
        String uniqueFormulasBucket = conf.get("formulas.bucket", "barik-cc");
        String uniqueFormulasKeyPrefix = conf.get("formulas.keyprefix", "analysis/formulas/");
    	
    	String fileName = value.toString().trim();
    	String path = importKeyPrefix + fileName;
    	if (fileName.isEmpty()) {
    		context.write(new Text(path), new Text("[Empty Path Name]"));
    		return;
    	}

    	try {
	        InputStream is = S3Load.loadSpreadsheet(importBucket, path);
	        
	        AnalysisOutputAndFormulas aof = SpreadsheetAnalyzer.doAnalysisAndGetObjectAndFormulas(is, corpusName, fileName);
	
	        JacksonS3Export.exportItem(aof.analysisObject, exportBucket, exportKeyPrefix, fileName);
	        if (! "true".equals(skipUniqueFormulas)) {
		        JacksonS3Export.exportItem(aof.uniqueFormulas, uniqueFormulasBucket, uniqueFormulasKeyPrefix, fileName);
	        }
	        aof.uniqueFormulas.clear(); 		//reduce memory footprint
	        
	        is.close();
	
	        context.write(new Text(path), new Text(aof.analysisObject.errorNotification));
    	}
    	catch (Exception e) {
    		context.write(new Text(path), new Text(e.toString()+" : "+Arrays.toString(e.getStackTrace())));
    	}

    }
}
