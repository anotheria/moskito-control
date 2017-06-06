package org.moskito.control.plugins.logfile.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to write status change logs in file
 */
public class LogFileAppender{

    /**
     * Hold output streams of log files
     */
    private static LogFilesHolder filesHolder = new LogFilesHolder();

    /**
     * Writes content to file specified by path.
     * Appends it to end of a file.
     *
     * @param path path to log file
     * @param content content to write in file
     * @throws IOException on I/O errors
     */
    public synchronized static void writeToFile(String path, String content) throws IOException {
        filesHolder.getOutputStreamByPath(path).write(content.getBytes());
    }

    /**
     * Class to hold output streams for log files
     * Init output stream on first call on unique file path.
     * Creates file and it`s directories if they not exists.
     */
    private static class LogFilesHolder{

        /**
         * Map to store output streams by file path
         * associated with stream
         */
        Map<String, FileOutputStream> logFiles = new HashMap<>();

        /**
         * Initialize file object by path to file.
         * Creates file and it`s directories if they not exist.
         *
         * @param path path to file
         * @return file object
         * @throws IOException if failed to create file (Invalid file path format)
         */
        private File initFile(String path) throws IOException {

            File file = new File(path);
            File parent = file.getParentFile();

            if (parent != null)
                parent.mkdirs();

            file.createNewFile();

            return file;

        }

        /**
         * Returns output stream associated with path in arguments.
         * @param path path to log file
         * @return output stream for file specified by path
         * @throws IOException on I/O errors
         */
        private OutputStream getOutputStreamByPath(String path) throws IOException {

            if(!logFiles.containsKey(path))
                logFiles.put(
                        path,
                        new FileOutputStream(initFile(path), true)
                );

            FileOutputStream fos = logFiles.get(path);

            fos.getChannel().position(
                    fos.getChannel().size()
            );

            return fos;

        }

    }

}
