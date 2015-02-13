package net.barik.spreadsheet;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class AnalysisJob {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        org.apache.hadoop.mapreduce.Job job = new org.apache.hadoop.mapreduce.Job();
        job.setJarByClass(AnalysisJob.class);

        job.setInputFormatClass(NLineInputFormat.class);
        job.setMapperClass(AnalysisMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        NLineInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
