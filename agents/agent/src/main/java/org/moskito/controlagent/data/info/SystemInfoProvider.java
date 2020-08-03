package org.moskito.controlagent.data.info;

import org.apache.commons.lang.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Singleton, that provides
 * information about monitored application.
 * Contains one public method {@link SystemInfoProvider#getSystemInfo()}
 * that returns {@link SystemInfo} instance with collected data.
 */
public class SystemInfoProvider {

    /**
     * Holds SystemInfoProvider instance
     */
    private static final SystemInfoProvider INSTANCE = new SystemInfoProvider();

    /**
     * Most of information fields is not changing
     * since app launch. This var holds {@link SystemInfo}
     * instance with this information.
     * Later copy of this object with
     */
    private SystemInfo initialInfo;

    private UptimeProvider uptimeProvider = new DefaultUptimeProvider();

    /**
     * Constructor, that builds initial info
     * object, filling java version, app launch command and
     * machine name.
     */
    private SystemInfoProvider(){

        initialInfo = new SystemInfo();

        initialInfo.setJavaVersion(
                System.getProperty("java.version")
        );
        initialInfo.setMachineName(
                getMachineName()
        );
        initialInfo.setStartCommand(
                getStartCommand()
        );
        initialInfo.setPid(
                getPid()
        );

    }

    public static SystemInfoProvider getInstance(){
        return INSTANCE;
    }

    /**
     * Returns {@link SystemInfo} object,
     * that contains data about this monitored app.
     *
     * Builds new {@link SystemInfo} from existing one:
     * this class singleton object has field with
     * {@link SystemInfo} instance, that already
     * contains some data occurred on class initialization.
     * Actually, only uptime updates here.
     *
     * @return {@link SystemInfo} filled with corresponding data
     */
    public SystemInfo getSystemInfo(){

        SystemInfo info = new SystemInfo();

        info.setJavaVersion(initialInfo.getJavaVersion());
        info.setMachineName(initialInfo.getMachineName());
        info.setStartCommand(initialInfo.getStartCommand());
        info.setPid(initialInfo.getPid());
        info.setUptime(uptimeProvider.getUptime());

        return info;

    }

    /**
     * Returns start command of this application.
     * Some data may not to be collected due
     * security settings and jvm  version.
     * TODO : IMPROVE THIS METHOD
     * @return start command of this app
     */
    private String getStartCommand(){

        String command;
        String classpath;
        String arguments;

        try{

            try { // trying to get start command using sun.java.command
                  // Work of this code may not to be guarantee on OpenJDK
                command = System.getProperty("sun.java.command");
            }
            catch (SecurityException| IllegalArgumentException e){
                command = "";
            }

            classpath = "-cp " + System.getProperty("java.class.path");

            // Getting start command arguments and joining it to single string
            arguments = StringUtils.join(
                            ManagementFactory.getRuntimeMXBean().getInputArguments(),
                            " "
                        );

        }
        catch (SecurityException| IllegalArgumentException e){
            return "Failed to get run command info due " + e.getClass().getName()
                    + ": " + e.getMessage();
        }

        return "java " + command + " " + classpath + " " + arguments;
    }

    /**
     * Returns machine name, where this app is launched
     * @return current machine name
     */
    private String getMachineName(){

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException | SecurityException e) {
            return "Unknown Computer";
        }

    }

    /**
     * Retrieves pid from MX beans
     * @return current process pid
     */
    private long getPid(){
        String processName =
                java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

	public void setUptimeProvider(UptimeProvider uptimeProvider) {
		this.uptimeProvider = uptimeProvider;
	}

}
