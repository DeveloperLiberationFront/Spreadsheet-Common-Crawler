package net.barik.spreadsheet;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.List;

public class DynamoDBExport {

    public static final String TABLE_NAME = "common-crawl-xls";

    public static void exportItems(List<WATExportModel> items) {
        DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient());
        Table table = dynamoDB.getTable(TABLE_NAME);

        for (WATExportModel watExportModel : items) {
            Item item = new Item()
                    .withString("path", watExportModel.getPath())
                    .withString("WARC-Target-URI", watExportModel.getWarcTargetURI())
                    .withString("WARC-Date", watExportModel.getWarcDate())
                    .withPrimaryKey("WARC-Record-ID", watExportModel.getWarcRecordId())
                    .withString("WARC-Refers-To", watExportModel.getWarcRefersTo())
                    .withLong("Content-Length", watExportModel.getContentLength())
                    .withJSON("Content", watExportModel.getContent());
            table.putItem(item);
        }
    }
}
