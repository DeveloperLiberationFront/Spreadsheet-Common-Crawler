package net.barik.spreadsheet;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SpreadsheetMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String path = value.toString();

        InputStream is = S3Load.loadWATStream(path);
        List<WATExportModel> list = WATParser.parse(path, is);
        JacksonS3Export.exportItems(list);

        is.close();

        for (WATExportModel watExportModel : list) {
            context.write(new Text(watExportModel.getWarcRecordId()), new Text(path));
        }
    }
}
