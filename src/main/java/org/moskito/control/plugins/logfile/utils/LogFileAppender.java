package org.moskito.control.plugins.logfile.utils;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.EchoEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Uses logback components to write status changes log
 */
public class LogFileAppender extends FileAppender<String>{

    private LogFileAppender(){}

    /**
     * Prepares encoder for file appender
     * @param path path to file to write logs
     * @return encoder for file appender
     * @throws IOException on I/O errors
     */
    private static EchoEncoder<String> prepareEncoder(String path) throws IOException {
        EchoEncoder<String> encoder = new EchoEncoder<>();
        encoder.init(new FileOutputStream(new File(path), true));
        return encoder;
    }

    /**
     * Writes content to specified by path file
     * using logback components
     *
     * @param path path to log file
     * @param content string to write in file
     *
     * @throws IOException on I/O errors
     */
    public static void writeToFile(String path, String content) throws IOException {

            LogFileAppender appender = new LogFileAppender();
            appender.setFile(path);
            appender.start();
            appender.setPrudent(true);
            appender.setEncoder(prepareEncoder(path));
            appender.writeOut(content);
            appender.stop();

    }

}
