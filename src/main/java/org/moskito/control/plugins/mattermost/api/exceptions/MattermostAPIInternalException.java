package org.moskito.control.plugins.mattermost.api.exceptions;

/**
 * Exception, that indicates internal Mattermost API wrapper error.
 * Means that there is a bug in api wrapper module.
 */
public class MattermostAPIInternalException extends Exception {

    public MattermostAPIInternalException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
