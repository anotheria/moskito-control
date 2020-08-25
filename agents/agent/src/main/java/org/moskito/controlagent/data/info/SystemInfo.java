package org.moskito.controlagent.data.info;

import java.io.Serializable;

/**
 * Contains information about monitored app  and its environment.
 */
public class SystemInfo implements Serializable{

    private static final long serialVersionUID = -4103365897826888898L;

    /**
     * Version of java
     */
    private String javaVersion;
    /**
     * Run command of monitored app
     */
    private String startCommand;
    /**
     * Name of machine, where monitored app is launched
     */
    private String machineName;
    /**
     * Monitored app uptime
     */
    private long uptime;
    /**
     * PID of current java process
     */
    private long pid;

    private float uphours;

    private float updays;

    SystemInfo(){}

    public String getJavaVersion() {
        return javaVersion;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public String getMachineName() {
        return machineName;
    }

    public long getUptime() {
        return uptime;
    }

    void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    void setMachineName(String machineName) {
        this.machineName = machineName;
    }

	public void setUptime(long uptime) {
		this.uptime = uptime;
		setUphours((float)uptime * 100 / (1000*60*60) / 100);
		setUpdays((float)uptime * 100 / (1000*60*60*24) / 100);
	}

	public float getUphours() {
		return uphours;
	}

	public void setUphours(float uphours) {
		this.uphours = uphours;
	}

	public float getUpdays() {
		return updays;
	}

	public void setUpdays(float updays) {
		this.updays = updays;
	}

	public String toString(){
        return "{" +
                    "javaVersion: "  + javaVersion  + ", " +
                    "startCommand: " + startCommand + ", " +
                    "machineName: "  + machineName  + ", " +
                    "uptime: "       + uptime + ", " +
					"uphours: "  	 + uphours + ", " +
					"updays: "		 + updays + ", " +
                    "PID: "          + pid +
               "}";
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }
}
