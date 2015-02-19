package net.barik.spreadsheet;

public class POIFastFilterDataModel {
    private String warcRecordId;
    private String detectedContentType;
    private String extension;
    private String digest;
    private long length;

    public POIFastFilterDataModel(String warcRecordId, String detectedContentType, String extension,
                                  String digest, long length) {
        this.warcRecordId = warcRecordId;
        this.detectedContentType = detectedContentType;
        this.extension = extension;
        this.digest = digest;
        this.length = length;
    }

    public String getDigest() {
        return "sha1:" + digest;
    }

    public long getLength() {
        return length;
    }

    public String getDetectedContentType() {
        return detectedContentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getWarcRecordId() {
        return warcRecordId;
    }

    public String getQualifiedWarcRecordId() {
        return "<urn:uuid:" + warcRecordId + ">";
    }
}

