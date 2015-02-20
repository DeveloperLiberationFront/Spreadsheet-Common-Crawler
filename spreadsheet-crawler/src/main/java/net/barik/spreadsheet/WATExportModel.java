package net.barik.spreadsheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONObject;

public class WATExportModel {

    private RecordIO importIO;
    private RecordIO exportIO;

    public WATExportModel(RecordIO importIO, RecordIO exportIO) {
        this.importIO = importIO;
        this.exportIO = exportIO;
    }

    public void exportItems(List<WATExportDataModel> items) throws IOException {
        for (WATExportDataModel watExportDataModel : items) {
            byte[] data = serializeModel(watExportDataModel);
            exportIO.save(getKeyForURI(watExportDataModel.getWarcRecordId()), data);
        }
    }

    public List<WATExportDataModel> parse(String resourceKey) throws IOException {
        InputStream inputStream = importIO.loadIntoMemory(resourceKey);
        List<WATExportDataModel> list = WATParser.parse(resourceKey, inputStream);

        return list;
    }

    public static byte[] serializeModel(WATExportDataModel model) throws IOException {
        JSONObject json = new JSONObject();

        json.put("Path", model.getPath());
        json.put("WARC-Target-URI", model.getWarcTargetURI());
        json.put("WARC-Date", model.getWarcDate());
        json.put("WARC-Record-ID", model.getWarcRecordId());
        json.put("WARC-Refers-To", model.getWarcRefersTo());
        json.put("WARC-Content-Type", model.getWarcContentType());
        json.put("Content-Length", model.getContentLength());
        json.put("Content", model.getContent());

        return json.toString().getBytes("UTF-8");
    }


    public static String getKeyForURI(String uri) {
        return uri.substring(10, uri.length() - 1);
    }
}
