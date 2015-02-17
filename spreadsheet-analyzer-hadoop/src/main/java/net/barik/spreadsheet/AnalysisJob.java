package net.barik.spreadsheet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class AnalysisJob extends Configured implements Tool {
	/**
	 * Usage:
	 * AnalysisJob listOfQualifiedS3Paths outputPathForJobStatus
	 */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new AnalysisJob(), args);
        System.exit(exitCode);
    }
    
    @Override
    public int run(String[] args) throws Exception {    
    	Configuration conf = getConf();
    	//use 100 lines per job instead of one.  It's fine for smaller jobs
        conf.set("mapreduce.input.lineinputformat.linespermap", "100");
 
        Job job = new Job(conf);
        
        job.setJarByClass(AnalysisJob.class);

        job.setInputFormatClass(NLineInputFormat.class);
        job.setMapperClass(AnalysisMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        NLineInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }
}
