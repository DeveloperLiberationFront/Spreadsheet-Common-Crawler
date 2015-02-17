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

    public static List<WATExportDataModel> parse(String path, InputStream inputStream) throws IOException {
        List<WATExportDataModel> list = new ArrayList<>();

        ArchiveReader ar = WARCReaderFactory.get(
                path,
                inputStream,
                true);

        for (ArchiveRecord record : ar) {
            if (record.getHeader().getMimetype().equals(MIME_TYPE)) {
                String content = IOUtils.toString(record);
                JSONObject json = new JSONObject(content);

                String warcTargetURI = record.getHeader().getHeaderValue("WARC-Target-URI").toString();

                if (warcTargetURI.toLowerCase().contains(".xls")
                        || hasWarcHeaderContentType(json)
                        || hasHttpResponseContentType(json)
                        || hasContentDisposition(json)) {
                    // After passing all the above filters, this is a loose candidate for a spreadsheet.
                    WATExportDataModel watExportDataModel = new WATExportDataModel(
                            path,
                            record.getHeader().getHeaderValue("WARC-Target-URI").toString(),
                            record.getHeader().getHeaderValue("WARC-Date").toString(),
                            record.getHeader().getHeaderValue("WARC-Record-ID").toString(),
                            record.getHeader().getHeaderValue("WARC-Refers-To").toString(),
                            record.getHeader().getHeaderValue("Content-Type").toString(),
                            Long.parseLong(record.getHeader().getHeaderValue("Content-Length").toString()),
                            json
                    );

                    list.add(watExportDataModel);
                }
            }
        }

        return list;
    }

    public static boolean hasWarcHeaderContentType(JSONObject json) {
        JSONObject envelope = json.getJSONObject("Envelope");
        JSONObject warcHeaderMetadata = envelope.getJSONObject("WARC-Header-Metadata");

        String contentType = warcHeaderMetadata.getString("Content-Type");

        String toProcess = contentType.toLowerCase().trim();

        if (toProcess.contains("application/vnd.ms-excel")
                || toProcess.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || toProcess.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.template")
                || toProcess.contains("application/vnd.ms-excel.sheet.macroEnabled.12")
                || toProcess.contains("application/vnd.ms-excel.template.macroEnabled.12")
                || toProcess.contains("application/vnd.ms-excel.addin.macroEnabled.12")
                || toProcess.contains("application/vnd.ms-excel.sheet.binary.macroEnabled.12")) {
            return true;
        }

        return false;
    }

    public static boolean hasHttpResponseContentType(JSONObject json) {
        JSONObject envelope = json.getJSONObject("Envelope");
        JSONObject payloadMetadata = envelope.getJSONObject("Payload-Metadata");

        if (!payloadMetadata.has("HTTP-Response-Metadata")) {
            return false;
        }

        JSONObject httpResponseMetadata = payloadMetadata.getJSONObject("HTTP-Response-Metadata");

        if (!httpResponseMetadata.has("Headers")) {
            return false;
        }

        JSONObject headers = httpResponseMetadata.getJSONObject("Headers");

        // HTTP responses don't standardize on Content-Type case.
        String contentType = null;
        if (headers.has("Content-Type")) {
            contentType = headers.getString("Content-Type");
        }
        else if (headers.has("Content-type")) {
            contentType = headers.getString("Content-type");
        }
        else if (headers.has("content-type")) {
            contentType = headers.getString("content-type");
        }

        if (contentType == null) {
            return false;
        }

        String toProcess = contentType.toLowerCase().trim();

        if (toProcess.contains("application/vnd.ms-excel")
                || toProcess.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || toProcess.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.template")
                || toProcess.contains("application/vnd.ms-excel.sheet.macroEnabled.12")
                || toProcess.contains("application/vnd.ms-excel.template.macroEnabled.12")
                || toProcess.contains("application/vnd.ms-excel.addin.macroEnabled.12")
                || toProcess.contains("application/vnd.ms-excel.sheet.binary.macroEnabled.12")) {
            return true;
        }

        return false;
    }

    public static boolean hasContentDisposition(JSONObject json) {
        JSONObject envelope = json.getJSONObject("Envelope");
        JSONObject payloadMetadata = envelope.getJSONObject("Payload-Metadata");

        if (!payloadMetadata.has("HTTP-Response-Metadata")) {
            return false;
        }

        JSONObject httpResponseMetadata = payloadMetadata.getJSONObject("HTTP-Response-Metadata");

        if (!httpResponseMetadata.has("Headers")) {
            return false;
        }

        JSONObject headers = httpResponseMetadata.getJSONObject("Headers");

        // Check content disposition, which seems to vary by case.
        String contentDisposition = null;
        if (headers.has("Content-Disposition")) {
            contentDisposition = headers.getString("Content-Disposition");
        }
        else if (headers.has("Content-disposition")) {
            contentDisposition = headers.getString("Content-disposition");
        }
        else if (headers.has("content-disposition")) {
            contentDisposition = headers.getString("content-disposition");
        }

        if (contentDisposition == null) {
            return false;
        }

        if (contentDisposition.toLowerCase().contains(".xls")) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:/data/commoncrawl/wat/" +
                "CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.wat.gz");

        // This comes from the paths files.
        String path = "common-crawl/crawl-data/CC-MAIN-2014-35/segments/" +
                "1408500800168.29/wat/CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.wat.gz";

        JSONObject json = new JSONObject().put("JSON", "Hello, World!");

        System.out.println(json.toString());

        System.out.println(json.opt("JSON"));
        System.out.println(json.opt("json"));

        // parse(path, fileInputStream);
    }
}
