package net.barik.spreadsheet;

import org.json.JSONObject;

public class WATExportDataModel {

    private String path;
    private String warcTargetURI;
    private String warcDate;
    private String warcRecordId;
    private String warcRefersTo;
    private String warcContentType;
    private long contentLength;

    private JSONObject content;

    public WATExportDataModel(
            String path,
            String warcTargetURI,
            String warcDate,
            String warcRecordId,
            String warcRefersTo,
            String warcContentType,
            long contentLength,
            JSONObject content) {
        this.path = path;
        this.warcTargetURI = warcTargetURI;
        this.warcDate = warcDate;
        this.warcRecordId = warcRecordId;
        this.warcRefersTo = warcRefersTo;
        this.warcContentType = warcContentType;
        this.contentLength = contentLength;
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public String getWarcTargetURI() {
        return warcTargetURI;
    }

    public String getWarcDate() {
        return warcDate;
    }

    public String getWarcRecordId() {
        return warcRecordId;
    }

    public String getWarcRefersTo() {
        return warcRefersTo;
    }

    public String getWarcContentType() {
        return warcContentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public JSONObject getContent() {
        return content;
    }
}
