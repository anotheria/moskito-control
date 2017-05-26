package org.moskito.control.plugins.logfile.utils;

import java.io.IOException;
import java.util.*;

/**
 * Thread-safe holder for log files.
 * Has method to retrieve file by its path
 */
public class StatusLogFileHolderThread extends Thread {

    private final Map<String, StatusLogFile> logFiles = new HashMap<>();

    public StatusLogFile getFileByPath(String path) throws IOException {

        synchronized (logFiles) {

            if (!logFiles.containsKey(path))
                logFiles.put(path, new StatusLogFile(path));

            return logFiles.get(path);

        }

    }

}
