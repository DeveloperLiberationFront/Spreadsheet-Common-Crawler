package net.barik.spreadsheet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.archive.io.ArchiveRecord;

interface RecordIO {
    InputStream load(String resourceKey);
    InputStream load(String resourceKey, long start, long end) throws IOException;
    InputStream loadIntoMemory(String resourceKey) throws IOException;
    File loadIntoFile(String resourceKey) throws IOException;
    void save(String resourceKey, byte[] data);
    void save(String resourceKey, ArchiveRecord record);
}
