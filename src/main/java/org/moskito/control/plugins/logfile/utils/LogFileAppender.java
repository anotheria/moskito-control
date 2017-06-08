package org.moskito.control.plugins.logfile.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    	OutputStream out = filesHolder.getOutputStreamByPath(path);
        out.write((content+"\n").getBytes(StandardCharsets.UTF_8));
        out.flush();

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
        @SuppressFBWarnings(value="RV_RETURN_VALUE_IGNORED_BAD_PRACTICE",
                justification = "Methods return false in case file/folders is not created. " +
                                "This may be caused by fact, that file/folders already exists." +
                                "If something really goes wrong createNewFile() throws IOException")
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
