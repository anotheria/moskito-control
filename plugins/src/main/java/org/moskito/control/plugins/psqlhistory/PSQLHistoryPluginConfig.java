package org.moskito.control.plugins.psqlhistory;

import org.configureme.annotations.ConfigureMe;

@ConfigureMe(allfields = true)
public class PSQLHistoryPluginConfig {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
