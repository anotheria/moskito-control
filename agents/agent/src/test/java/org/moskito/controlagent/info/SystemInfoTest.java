package org.moskito.controlagent.info;

import org.junit.Test;
import org.moskito.controlagent.data.info.DefaultUptimeProvider;
import org.moskito.controlagent.data.info.SystemInfo;
import org.moskito.controlagent.data.info.SystemInfoProvider;

import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SystemInfoTest {

    private SystemInfo getInfo(){
        return SystemInfoProvider.getInstance().getSystemInfo();
    }

    private int getUptimeStringSeconds(String uptime){
        String[] uptimeComponents = uptime.split(",");
        return Integer.valueOf(uptimeComponents[3]);
    }

    @Test
    public void testMachineName() throws UnknownHostException {

        String machineName = getInfo().getMachineName();
        // "Unknown Computer" means name not found.
        assertNotEquals("Unknown Computer", machineName);
        System.out.println("Machine name : " + machineName);

    }

    @Test
    public void testUptime() throws InterruptedException {

        final String uptimeRegexpPattern = "^\\d+,\\d+,\\d+,\\d+$";

        long firstUptime = getInfo().getUptime();
        Thread.sleep(100);
        long secondUptime = getInfo().getUptime();

        assertNotEquals(firstUptime, secondUptime);
        // Asserting that uptimes differ at least on one second
        assertTrue(
                (secondUptime) - (firstUptime) >= 100
        );


		//System.out.println("First uptime : " + firstUptime + ", second uptime after 1s sleep : " + secondUptime);

    }

    @Test public void testHourCalculation(){
    	long uptime = (long)(1000*3600*3.5);

    	SystemInfoProvider.getInstance().setUptimeProvider(new TestUptimeProvider(uptime));
    	SystemInfo info1 = SystemInfoProvider.getInstance().getSystemInfo();
    	assertEquals(uptime, info1.getUptime());
    	assertEquals(3.5, info1.getUphours(), 0.01f);
    	assertEquals(0.14, info1.getUpdays(), 0.01f);


		uptime = (long)(1000*3600*24*3);

		SystemInfoProvider.getInstance().setUptimeProvider(new TestUptimeProvider(uptime));
		SystemInfo info2 = SystemInfoProvider.getInstance().getSystemInfo();
		assertEquals(uptime, info2.getUptime());
		assertEquals(72, info2.getUphours(), 0.01f);
		assertEquals(3, info2.getUpdays(), 0.01f);


    	//reset uptime provider
		SystemInfoProvider.getInstance().setUptimeProvider(new DefaultUptimeProvider());
	}

}
