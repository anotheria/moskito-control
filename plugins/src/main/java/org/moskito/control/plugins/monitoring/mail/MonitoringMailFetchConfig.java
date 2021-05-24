package org.moskito.control.plugins.monitoring.mail;

import org.configureme.annotations.Configure;

public class MonitoringMailFetchConfig {

    @Configure
    private String host;
    @Configure
    private int port = 995;
    @Configure
    private boolean starttlsEnable = true;

    @Configure
    private String user;
    @Configure
    private String password;

    @Configure
    private String folder = "INBOX";
    /**
     * Plugin will monitor only emails with that subject. Optional.
     */
    @Configure
    private String mailSubject;
    /**
     * Limit how many messages plugin will iterate from the last to find mail with given subject.
     * Optional, but if you specified mail subject and you know that mail box can contain a lod of messages,
     * better to set some limit.
     */
    @Configure
    private Integer mailSubjectSearchLimit;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isStarttlsEnable() {
        return starttlsEnable;
    }

    public void setStarttlsEnable(boolean starttlsEnable) {
        this.starttlsEnable = starttlsEnable;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public Integer getMailSubjectSearchLimit() {
        return mailSubjectSearchLimit;
    }

    public void setMailSubjectSearchLimit(Integer mailSubjectSearchLimit) {
        this.mailSubjectSearchLimit = mailSubjectSearchLimit;
    }

    @Override
    public String toString() {
        return "MonitoringMailFetchConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", starttlsEnable=" + starttlsEnable +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", folder='" + folder + '\'' +
                ", mailSubject='" + mailSubject + '\'' +
                ", mailSubjectSearchLimit=" + mailSubjectSearchLimit +
                '}';
    }
}
