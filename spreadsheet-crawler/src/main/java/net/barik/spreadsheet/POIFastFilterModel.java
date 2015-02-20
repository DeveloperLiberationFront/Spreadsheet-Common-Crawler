package net.barik.spreadsheet;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.json.JSONObject;

// http://standards.freedesktop.org/shared-mime-info-spec/
public class POIFastFilterModel {

    private RecordIO importIO;
    private RecordIO exportIO;

    public POIFastFilterModel(RecordIO importIO, RecordIO exportIO) {
        this.importIO = importIO;
        this.exportIO = exportIO;
    }

    public POIFastFilterDataModel parse(String warcRecordId) throws IOException, MimeTypeException {

        InputStream inputStream = importIO.load(warcRecordId);
        TikaConfig config = TikaConfig.getDefaultConfig();

        byte[] bytes = IOUtils.toByteArray(inputStream);
        TikaInputStream tikaInputStream = TikaInputStream.get(bytes);

        // Use a composite detector, which attempts best-to-worse matching. Because our metadata is
        // unreliable, we pass in an empty Metadata object.
        MediaType mediaType = new DefaultDetector().detect(tikaInputStream, new Metadata());
        MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());

        POIFastFilterDataModel model = new POIFastFilterDataModel(warcRecordId, mimeType.toString(),
                mimeType.getExtension(), DigestUtils.shaHex(bytes), bytes.length);

        IOUtils.closeQuietly(tikaInputStream);

        return model;
    }

    public static boolean isValidSpreadsheet(POIFastFilterDataModel model) {
        switch (model.getDetectedContentType()) {
            case "application/vnd.ms-excel":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.template":
            case "application/vnd.ms-excel.sheet.macroEnabled.12":
            case "application/vnd.ms-excel.template.macroEnabled.12":
            case "application/vnd.ms-excel.addin.macroEnabled.12":
            case "application/vnd.ms-excel.sheet.binary.macroEnabled.12":
                return true;
            default:
                return false;
        }
    }

    public static byte[] serializeModel(POIFastFilterDataModel model) throws IOException {
        JSONObject json = new JSONObject();

        json.put("WARC-Record-ID", model.getQualifiedWarcRecordId());
        json.put("Tika-Content-Type", model.getDetectedContentType());
        json.put("Tika-Extension", model.getExtension());
        json.put("Digest", model.getDigest());
        json.put("Length", model.getLength());

        return json.toString().getBytes("UTF-8");
    }

    public void export(POIFastFilterDataModel model) throws IOException {
        byte[] data = serializeModel(model);
        exportIO.save(model.getWarcRecordId(), data);
    }

    public static void main(String[] args) throws IOException, MimeTypeException {
        RecordIO importIO = new LocalRecordIO("./");
        POIFastFilterModel model = new POIFastFilterModel(importIO, null);
        POIFastFilterDataModel dataModel =  model.parse("sample/password.xls");

        System.out.println(dataModel.getQualifiedWarcRecordId());
        System.out.println(dataModel.getDetectedContentType());
        System.out.println(dataModel.getExtension());

    }
}
