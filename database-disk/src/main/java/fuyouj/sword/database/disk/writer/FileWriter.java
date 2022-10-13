package fuyouj.sword.database.disk.writer;

public interface FileWriter {
    void append(Object object);

    void close();

    byte[] format(Object object);

    void setFile(String fileName);

}
