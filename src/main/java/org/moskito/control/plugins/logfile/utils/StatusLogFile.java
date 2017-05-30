package org.moskito.control.plugins.logfile.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

/**
 * Manages log file.
 * Creates file and it parent folder if they not exist.
 * Has thread-safe method to write content to log file
 */
public class StatusLogFile {

    private final File file;

    /**
     * Initialize file field.
     * Creates file and it parent folder is they not exist
     * @param path path to log file
     * @throws IOException on create file errors
     */
    public StatusLogFile(String path) throws IOException, SecurityException {

        file = new File(path);

        // Create directories of file if they not exists
        File parentFile = file.getParentFile();
        if(parentFile != null) parentFile.mkdirs();

        // Create file if it not exist
        file.createNewFile();

    }

    /**
     * Thread-safe method to write content to log file
     * @param content string to write in file
     * @throws IOException on I/O errors
     */
    public void writeToFile(String content) throws IOException {

        synchronized (file) {

            FileLock lock = null;

            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {

                lock = randomAccessFile.getChannel().lock();
                randomAccessFile.seek(file.length());
                randomAccessFile.writeChars(content);
                lock.release();
                randomAccessFile.close();

            } catch (OverlappingFileLockException e) {
                throw new IOException("Failed to get lock on file " + file.getPath());
            } finally {
                if (lock != null && lock.channel().isOpen()) lock.release();
            }

        }

    }

}
