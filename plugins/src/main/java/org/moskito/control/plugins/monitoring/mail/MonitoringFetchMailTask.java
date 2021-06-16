package org.moskito.control.plugins.monitoring.mail;

import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Flags;
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
        Result result = new Result(config);
        try {
            //create properties field
            Properties properties = getMailProperties(fetchConfig);
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(fetchConfig.getHost(), fetchConfig.getUser(), fetchConfig.getPassword());

            //create the folder object and open it
            Folder emailFolder = store.getFolder(fetchConfig.getFolder());
            emailFolder.open(Folder.READ_WRITE);

            // get last message date
            result.setLastMessageDate(getLastMailMessageDate(emailFolder, fetchConfig));

            boolean deleteMessages = fetchConfig.isDeleteWithSubject() && fetchConfig.getMailSubject() != null;
            if (deleteMessages) {
                deleteMessagesWithSubject(emailFolder, fetchConfig.getMailSubject(), fetchConfig.getMailSubjectSearchLimit());
            }

            //close the store and folder objects
            emailFolder.close(deleteMessages);
            store.close();
        } catch (Exception e) {
            log.error("getLastMailMessageDate(). config: {}. cause: {}", fetchConfig.getUser(), e.getMessage(), e);
        }

        return result;
    }

    private Date getLastMailMessageDate(Folder emailFolder, MonitoringMailFetchConfig config) {
        Date result = null;
        try {
            String monitoringSubject = config.getMailSubject();
            // find last message
            Message message = StringUtils.isEmpty(monitoringSubject) ?
                    getLastMessage(emailFolder) :
                    getLastMessageWithSubject(emailFolder, monitoringSubject, config.getMailSubjectSearchLimit());

            result = message == null ? null : message.getSentDate();
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

    private void deleteMessagesWithSubject(Folder emailFolder, String subject, Integer mailSubjectSearchLimit) throws MessagingException {
        Message[] messages = emailFolder.getMessages();

        // in there is no email in given limit mails let's think that there no such mail
        if (mailSubjectSearchLimit != null && messages.length > mailSubjectSearchLimit) {
            messages = Arrays.copyOfRange(messages, messages.length - mailSubjectSearchLimit, messages.length);
        }

        Integer lastMessageIndex = null;

        // search message from the last
        for (int i = messages.length - 1; i > 0; i--) {
            Message message = messages[i];
            if (!message.getSubject().equals(subject)) {
                continue;
            }

            // if not the last message mark as deleted
            // the last message used to get last message stats
            if (lastMessageIndex != null) {
                message.setFlag(Flags.Flag.DELETED, true);
            } else {
                lastMessageIndex = i;
            }
        }
    }

    private Properties getMailProperties(MonitoringMailFetchConfig config) {
        Properties properties = new Properties();

        properties.put("mail.pop3.host", config.getHost());
        properties.put("mail.pop3.port", config.getPort() + "");
        properties.put("mail.pop3.starttls.enable", config.isStarttlsEnable() + "");
        properties.put("mail.pop3.ssl.protocols", "TLSv1.2");
        properties.put("mail.pop3.ssl.trust", "*");
        return properties;
    }

    /**
     * Mail task result.
     */
    public static class Result {
        /**
         * Last massage from mailbox.
         */
        private Date lastMessageDate;
        /**
         * Config that was used to check.
         */
        private final MonitoringMailConfig config;

        public Result(MonitoringMailConfig config) {
            this.config = config;
        }

        public Result(Date lastMessageDate, MonitoringMailConfig config) {
            this.lastMessageDate = lastMessageDate;
            this.config = config;
        }

        public Date getLastMessageDate() {
            return lastMessageDate;
        }

        public void setLastMessageDate(Date lastMessageDate) {
            this.lastMessageDate = lastMessageDate;
        }

        public MonitoringMailConfig getConfig() {
            return config;
        }

    }

}
