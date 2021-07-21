package org.moskito.control.plugins.monitoring.mail;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

@Ignore
public class MonitoringMailPluginTest {

    @Test
    public void monitoringFetchMailTaskTest() {
        MonitoringMailConfig config = new MonitoringMailConfig();
        MonitoringMailFetchConfig fetchConfig = new MonitoringMailFetchConfig();
        fetchConfig.setHost("mx2.java-performance.guru");
        fetchConfig.setPort(995);
        fetchConfig.setStarttlsEnable(true);
        fetchConfig.setFolder("INBOX");
        fetchConfig.setUser("mail@anotheria.net");
        fetchConfig.setPassword("xxxxxx");

        fetchConfig.setMailSubject("Mail api check");
        fetchConfig.setMailSubjectSearchLimit(100);


        MonitoringFetchMailTask.Result result = new MonitoringFetchMailTask(config).execute();
        Date lastMessageDate = result.getLastMessageDate();
        System.out.println("lastMessageDate: " + lastMessageDate);
    }
}
