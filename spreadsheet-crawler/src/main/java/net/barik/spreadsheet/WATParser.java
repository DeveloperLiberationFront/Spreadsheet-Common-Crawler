package net.barik.spreadsheet;

import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WATParser {
    private static final String MIME_TYPE = "application/json";
    private static final String FILTER_CONTENT = "application/vnd.ms-excel";

    public static List<WATExportModel> parse(String path, InputStream inputStream) throws IOException {
        List<WATExportModel> list = new ArrayList<>();

        ArchiveReader ar = WARCReaderFactory.get(
                path,
                inputStream,
                true);

        for (ArchiveRecord record : ar) {
            if (record.getHeader().getMimetype().equals(MIME_TYPE)) {
                String content = IOUtils.toString(record);
                JSONObject json = new JSONObject(content);

                JSONObject envelope = json.getJSONObject("Envelope");
                JSONObject payloadMetadata = envelope.getJSONObject("Payload-Metadata");

                if (!payloadMetadata.has("HTTP-Response-Metadata")) {
                    continue;
                }

                JSONObject httpResponseMetadata = payloadMetadata.getJSONObject("HTTP-Response-Metadata");

                if (!httpResponseMetadata.has("Headers")) {
                    continue;
                }

                JSONObject headers = httpResponseMetadata.getJSONObject("Headers");

                if (!headers.has("Content-Type")) {
                    continue;
                }

                String contentType = headers.getString("Content-Type");

                if (contentType.equals(FILTER_CONTENT)) {
                    // After passing all the above filters, this is probably a spreadsheet.
                    WATExportModel watExportModel = new WATExportModel(
                            path,
                            record.getHeader().getHeaderValue("WARC-Target-URI").toString(),
                            record.getHeader().getHeaderValue("WARC-Date").toString(),
                            record.getHeader().getHeaderValue("WARC-Record-ID").toString(),
                            record.getHeader().getHeaderValue("WARC-Refers-To").toString(),
                            record.getHeader().getHeaderValue("Content-Type").toString(),
                            Long.parseLong(record.getHeader().getHeaderValue("Content-Length").toString()),
                            content
                    );

                    list.add(watExportModel);
                }
            }
        }

        return list;
    }

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:/data/commoncrawl/wat/" +
                "CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.wat.gz");

        // This comes from the paths files.
        String path = "common-crawl/crawl-data/CC-MAIN-2014-35/segments/" +
                "1408500800168.29/wat/CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.wat.gz";

        parse(path, fileInputStream);
    }
}
