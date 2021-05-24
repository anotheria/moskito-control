package org.moskito.control.plugins.monitoring.mail;

import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * Fetch given mail and returns monitoring result.
 *
 * @author ynikonchuk
 */
public class MonitoringFetchMailTask {

    private static final Logger log = LoggerFactory.getLogger(MonitoringFetchMailTask.class);

    private final MonitoringMailConfig config;

    public MonitoringFetchMailTask(MonitoringMailConfig config) {
        this.config = config;
    }

    /**
     * Executes task.
     *
     * @return {@link Result}
     */
    public Result execute() {
        MonitoringMailFetchConfig fetchConfig = config.getFetchMailConfig();
        if (fetchConfig == null) {
            log.debug("no fetchMailConfig for: " + config.getName());
            return new Result(null, config);
        }

        log.info("execute(). config: " + config.getName());
        Date lastMailMessageDate = getLastMailMessageDate(fetchConfig);
        return new Result(lastMailMessageDate, config);
    }

    private Date getLastMailMessageDate(MonitoringMailFetchConfig config) {
        Date result = null;
        try {
            //create properties field
            Properties properties = getMailProperties(config);
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(config.getHost(), config.getUser(), config.getPassword());

            //create the folder object and open it
            Folder emailFolder = store.getFolder(config.getFolder());
            emailFolder.open(Folder.READ_ONLY);

            String monitoringSubject = config.getMailSubject();
            // find last message
            Message message = StringUtils.isEmpty(monitoringSubject) ?
                    getLastMessage(emailFolder) :
                    getLastMessageWithSubject(emailFolder, monitoringSubject, config.getMailSubjectSearchLimit());

            result = message == null ? null : message.getSentDate();

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            log.error("getLastMailMessageDate(). config: {}. cause: {}", config.getUser(), e.getMessage(), e);
        }
        return result;
    }

    /**
     * Returns any last message from given folder.
     *
     * @param emailFolder {@link Folder}
     * @return {@link Message}
     * @throws MessagingException in case of error
     */
    private Message getLastMessage(Folder emailFolder) throws MessagingException {
        if (emailFolder.getMessageCount() == 0) {
            return null;
        }
        return emailFolder.getMessage(emailFolder.getMessageCount());
    }

    /**
     * Returns last message with given subject.
     *
     * @param emailFolder            {@link Folder}
     * @param subject                subject to find
     * @param mailSubjectSearchLimit how many messages (from the last) should be checked.
     * @return {@link Message}
     * @throws MessagingException in case of error
     */
    private Message getLastMessageWithSubject(Folder emailFolder, String subject, Integer mailSubjectSearchLimit) throws MessagingException {
        Message[] messages = emailFolder.getMessages();

        // in there is no email in given limit mails let's think that there no such mail
        if (mailSubjectSearchLimit != null && messages.length > mailSubjectSearchLimit) {
            messages = Arrays.copyOfRange(messages, messages.length - mailSubjectSearchLimit, messages.length);
        }

        // search message from the last
        for (int i = messages.length - 1; i > 0; i--) {
            Message message = messages[i];

            if (message.getSubject().equals(subject)) {
                return message;
            }
        }
        return null;
    }

    private Properties getMailProperties(MonitoringMailFetchConfig config) {
        Properties properties = new Properties();

        properties.put("mail.pop3.host", config.getHost());
        properties.put("mail.pop3.port", config.getPort() + "");
        properties.put("mail.pop3.starttls.enable", config.isStarttlsEnable() + "");
        properties.put("mail.pop3.ssl.protocols", "TLSv1.2");
        return properties;
    }

    /**
     * Mail task result.
     */
    public static class Result {
        /**
         * Last massage from mailbox.
         */
        private final Date lastMessageDate;
        /**
         * Config that was used to check.
         */
        private final MonitoringMailConfig config;

        public Result(Date lastMessageDate, MonitoringMailConfig config) {
            this.lastMessageDate = lastMessageDate;
            this.config = config;
        }

        public Date getLastMessageDate() {
            return lastMessageDate;
        }

        public MonitoringMailConfig getConfig() {
            return config;
        }

    }

}
