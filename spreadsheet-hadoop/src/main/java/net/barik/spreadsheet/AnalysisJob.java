package net.barik.spreadsheet;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class AnalysisJob {
	/**
	 * Usage:
	 * AnalysisJob listOfQualifiedS3Paths S3OutputDir outputPathForJobStatus
	 */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        org.apache.hadoop.mapreduce.Job job = new org.apache.hadoop.mapreduce.Job();
        
        JacksonS3Export.setS3OutputPath(args[1]);
        job.setJarByClass(AnalysisJob.class);

        job.setInputFormatClass(NLineInputFormat.class);
        job.setMapperClass(AnalysisMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        NLineInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
