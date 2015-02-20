package net.barik.spreadsheet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;
import org.archive.io.HeaderedArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;

public class ParseExcelWARC {
    public static void main(String[] args) {
        try {
//            FileInputStream fis = new FileInputStream("D:/data/commoncrawl/" +
//                    "CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.gz");
//
//            ArchiveReader ar =
//                    WARCReaderFactory.get("D:/data/commoncrawl/" +
//                            "CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.gz");

            FileInputStream fis = new FileInputStream("D:/data/commoncrawl/" +
                    "CC-MAIN-20140820021320-00001-ip-10-180-136-8.ec2.internal.warc.gz");

            ArchiveReader ar =
                    WARCReaderFactory.get("D:/data/commoncrawl/" +
                            "CC-MAIN-20140820021320-00001-ip-10-180-136-8.ec2.internal.warc.gz");

//            ArchiveRecord record = ar.get(639684167);
            ArchiveRecord record = ar.get(166162707);

            System.out.println("Identifier: " + ar.getReaderIdentifier());

            HeaderedArchiveRecord hr = new HeaderedArchiveRecord(record, true);
            ArchiveRecordHeader header = hr.getHeader();

            String recordId = header.getHeaderValue("WARC-Record-ID").toString();

            // Sample: WARC-Record-ID=<urn:uuid:0edb2c40-f990-4399-a765-9a9a5d4a12d5>
            recordId = recordId.substring(10, recordId.length() - 1);

            System.out.println("Full header: " + header.toString());

            FileOutputStream fos = new FileOutputStream(recordId);
            hr.dump(fos);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
