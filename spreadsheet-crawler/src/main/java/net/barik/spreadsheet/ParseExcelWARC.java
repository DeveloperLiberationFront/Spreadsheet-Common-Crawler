package net.barik.spreadsheet;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.archive.format.http.HttpResponseMessage;
import org.archive.format.http.HttpResponseMessageParser;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;
import org.archive.io.HeaderedArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.eclipse.jdt.internal.core.SourceType;

import java.io.*;
import java.util.Iterator;
import java.util.UUID;

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
            System.out.println(recordId);


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
