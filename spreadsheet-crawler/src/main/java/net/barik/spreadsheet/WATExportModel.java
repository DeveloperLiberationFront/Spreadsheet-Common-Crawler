package net.barik.spreadsheet;

public class WATExportModel {

    private String path;
    private String warcTargetURI;
    private String warcDate;
    private String warcRecordId;
    private String warcRefersTo;
    private String warcContentType;
    private long contentLength;

    private String content;

    public WATExportModel(
            String path,
            String warcTargetURI,
            String warcDate,
            String warcRecordId,
            String warcRefersTo,
            String warcContentType,
            long contentLength,
            String content) {
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

    public String getContent() {
        return content;
    }
}
