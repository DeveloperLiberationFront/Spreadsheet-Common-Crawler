package net.barik.spreadsheet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecordHeader;
import org.archive.io.HeaderedArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.json.JSONObject;

public class WARCExportModel {
    private JSONObject jsonRoot;

    private RecordIO importIO;
    private RecordIO exportIO;

    private String targetResourceKey;

    public WARCExportModel(JSONObject jsonRoot, RecordIO importIO, RecordIO exportIO) {
        this.jsonRoot = jsonRoot;

        this.importIO = importIO;
        this.exportIO = exportIO;
    }

    public static String watSegment(String watPath) {
        int endIndex = watPath.indexOf("/wat/");
        String watSegment = watPath.substring(0, endIndex);

        return watSegment;
    }

    public void process() throws IOException {
        // Parse the JSON file.
        JSONObject jsonContent = jsonRoot.getJSONObject("Content");
        JSONObject jsonContainer = jsonContent.getJSONObject("Container");

        String watPath = jsonRoot.getString("Path");
        String warcFilename = jsonContainer.getString("Filename");
        String warcPath = watSegment(watPath) + "/warc/" + warcFilename;

        long warcOffset = jsonContainer.getLong("Offset");

        String watRecordId = jsonRoot.getString("WARC-Record-ID");
        String warcRefersTo = jsonRoot.getString("WARC-Refers-To");

        JSONObject jsonGzipMetadata = jsonContainer.getJSONObject("Gzip-Metadata");
        long warcDeflateLength = jsonGzipMetadata.getLong("Deflate-Length");

        // Grab the record range from the corresponding WARC file.
        InputStream warcFullArchive = new BufferedInputStream(
                importIO.load(warcPath, warcOffset, warcOffset + warcDeflateLength - 1));

        // Take the GZIP range and extract it into an ArchiveReader object.
        ArchiveReader ar = WARCReaderFactory.get(watPath, warcFullArchive, false);

        // Decorate the record in a header object, since we need to parse the headers.
        HeaderedArchiveRecord hr = new HeaderedArchiveRecord(ar.get(), true);
        ArchiveRecordHeader header = hr.getHeader();

        // Example: WARC-Record-ID=<urn:uuid:0edb2c40-f990-4399-a765-9a9a5d4a12d5>
        String recordId = header.getHeaderValue("WARC-Record-ID").toString();
        this.targetResourceKey = recordId.substring(10, recordId.length() - 1);

        // Sanity check to make sure we have the correct record. If these don't match, then the offset
        // is wrong.
        if (!warcRefersTo.equals(recordId)) {
            throw new IOException("Mismatch between: " + recordId + " " + warcRefersTo);
        }

        // When we save, use the WAT identifier for continuity.
        String saveId = watRecordId.substring(10, watRecordId.length() - 1);
        exportIO.save(saveId, hr);

        warcFullArchive.close();
        hr.close();
    }

    public String getTargetResourceKey() {
        return targetResourceKey != null ? targetResourceKey : "";
    }
}
