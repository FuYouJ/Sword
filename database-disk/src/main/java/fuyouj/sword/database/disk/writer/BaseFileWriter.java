package fuyouj.sword.database.disk.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fuyouj.sword.database.utils.JacksonFactory;
import com.fuyouj.sword.scabard.Jsons;
import com.fuyouj.sword.scabard.command.Loggable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseFileWriter implements Loggable, FileWriter {
    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    protected int bufferSize = DEFAULT_BUFFER_SIZE;
    protected boolean bufferedIO = false;
    protected boolean fileAppendOnly = true;
    protected String fileName;
    protected OutputStream outputStream;
    private File file;
    private FileOutputStream fos;

    public BaseFileWriter(final String fileName) {
        this.fileName = fileName;
        setFile(fileName);
    }

    public void append(final Object object) {
        check();

        try {
            this.outputStream.write(this.format(object));
            if (this.isImmediateFlush()) {
                this.outputStream.flush();
            }
        } catch (IOException e) {
            error("Failed to write to file: " + this.fileName, e);
        }
    }

    public void close() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                // There is do need to invoke an error handler at this late
                // stage.
                error("Could not close " + this.outputStream.getClass().getName(), e);
            }
        }
    }

    public byte[] format(final Object object) {
        return (asJsonString(object) + LINE_SEPARATOR)
                .getBytes(StandardCharsets.UTF_8);
    }

    public void setFile(final String fileName) {
        reset();
        FileOutputStream ostream = null;
        String parentName = new File(fileName).getParent();
        if (parentName != null) {
            File parentDir = new File(parentName);
            try {
                file = new File(fileName);
                if (parentDir.exists()) {
                    ostream = new FileOutputStream(file, fileAppendOnly);
                } else if (!parentDir.exists() && parentDir.mkdirs()) {
                    ostream = new FileOutputStream(file, fileAppendOnly);
                } else {
                    error("Failed to create directory: " + parentName);
                }
            } catch (FileNotFoundException e) {
                error("Could not create file: " + fileName, e);
            }
        } else {
            error("Could not create file: " + fileName);
        }

        fos = ostream;
        if (bufferedIO) {
            outputStream = new BufferedOutputStream(fos, bufferSize);
        } else {
            outputStream = fos;
        }
    }

    protected void attemptRecovery() {
        this.close();

        // subsequent writes must always be in append mode
        try {
            outputStream = openNewOutputStream();
        } catch (IOException e) {
            error("Failed to open file: " + this.fileName, e);
        }
    }

    protected void check() {
        if (this.outputStream == null) {
            attemptRecovery();
        }
    }

    protected String getFormatFileName() {
        return fileName + ".json";
    }

    protected boolean isImmediateFlush() {
        return !bufferedIO;
    }

    protected OutputStream openNewOutputStream() throws IOException {
        // see LOGBACK-765
        fos = new FileOutputStream(file, true);
        return new BufferedOutputStream(fos);
    }

    protected void reset() {
        close();
        this.file = null;
        this.fos = null;
        this.outputStream = null;
    }

    private String asJsonString(final Object object) {
        String json;
        try {
            json = Jsons.toJsonString(object, JacksonFactory.DEFAULT, true);
        } catch (JsonProcessingException e) {
            try {
                json = Jsons.toJsonString(object, true);
            } catch (JsonProcessingException ex) {
                error("Failed to convert object to json", ex);
                return "";
            }
        }

        return json;
    }
}
