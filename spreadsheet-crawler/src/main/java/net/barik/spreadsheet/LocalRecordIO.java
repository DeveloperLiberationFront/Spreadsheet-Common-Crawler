package net.barik.spreadsheet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.archive.io.ArchiveRecord;

import java.io.*;

public class LocalRecordIO implements RecordIO {
    private String filePath;

    public LocalRecordIO(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public InputStream load(String resourceKey) {
        try {
            return new FileInputStream(new File(filePath, resourceKey));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public InputStream load(String resourceKey, long start, long end) {
        throw new NotImplementedException();
    }

    @Override
    public File loadIntoFile(String resourceKey) throws IOException {
        return null;
    }

    @Override
    public InputStream loadIntoMemory(String resourceKey) throws IOException {
        return null;
    }

    @Override
    public void save(String resourceKey, byte[] data) {
        try (FileOutputStream out = new FileOutputStream(new File(filePath, resourceKey))) {
            IOUtils.write(data, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String resourceKey, ArchiveRecord record)  {
        try (FileOutputStream fos = new FileOutputStream(new File(filePath, resourceKey))) {
            record.dump(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
