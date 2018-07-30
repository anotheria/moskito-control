package org.moskito.control.data.thresholds;

import net.anotheria.moskito.core.threshold.ThresholdStatus;
import org.junit.Before;
import org.junit.Test;
import org.moskito.control.core.Application;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class DataThresholdTest {

	private String name;
	private List<String> lines;
	private DataThreshold threshold;

	@Before
	public void createSettingLines() {
		lines = new LinkedList<>();
		lines.add("varName DOWN 10 GREEN");
		lines.add("varName UP 10 YELLOW");
		lines.add("varName UP 20 ORANGE");
		lines.add("varName UP 30 RED");
		lines.add("varName UP 40 PURPLE");

		name = "varName";

		threshold = new DataThreshold();
		threshold.configure(name, lines);
	}

	@Test
	public void testNewThresholdStatusAndValue() {
		assertEquals(ThresholdStatus.OFF, threshold.getStatus());
		assertEquals(ThresholdStatus.OFF, threshold.getPreviousStatus());
		assertEquals("none yet", threshold.getValue());
		assertEquals("none yet", threshold.getPreviousValue());
		assertTrue(threshold.getTimestamp() > 0);
		assertEquals(0, threshold.getFlipCount());
	}

	@Test
	public void testGetNewStatus() {
		assertEquals(ThresholdStatus.OFF, threshold.getStatus());
		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("-1"));
		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("0"));
		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("1"));
		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("10"));
		assertEquals(ThresholdStatus.YELLOW, threshold.getNewStatus("11"));
		assertEquals(ThresholdStatus.YELLOW, threshold.getNewStatus("20"));
		assertEquals(ThresholdStatus.ORANGE, threshold.getNewStatus("21"));
		assertEquals(ThresholdStatus.ORANGE, threshold.getNewStatus("30"));
		assertEquals(ThresholdStatus.RED, threshold.getNewStatus("31"));
		assertEquals(ThresholdStatus.RED, threshold.getNewStatus("40"));
		assertEquals(ThresholdStatus.PURPLE, threshold.getNewStatus("41"));

		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("-1.2"));
		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("0.0"));
		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("9.99"));
		assertEquals(ThresholdStatus.YELLOW, threshold.getNewStatus("10.1"));
		assertEquals(ThresholdStatus.YELLOW, threshold.getNewStatus("19.9"));
		assertEquals(ThresholdStatus.ORANGE, threshold.getNewStatus("20.1"));
		assertEquals(ThresholdStatus.ORANGE, threshold.getNewStatus("29.9999"));
		assertEquals(ThresholdStatus.RED, threshold.getNewStatus("30.001"));
		assertEquals(ThresholdStatus.RED, threshold.getNewStatus("39.0"));
		assertEquals(ThresholdStatus.PURPLE, threshold.getNewStatus("40.1"));

		threshold.setStatus(ThresholdStatus.GREEN);
		assertEquals(ThresholdStatus.GREEN, threshold.getStatus());
		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("1"));
		assertEquals(ThresholdStatus.RED, threshold.getNewStatus("35"));

		threshold.setStatus(ThresholdStatus.RED);
		assertEquals(ThresholdStatus.GREEN, threshold.getNewStatus("1"));
	}

	@Test
	public void testUpdate() {
		assertEquals(ThresholdStatus.OFF, threshold.getStatus());
		assertEquals(ThresholdStatus.OFF, threshold.getPreviousStatus());
		assertEquals("none yet", threshold.getValue());
		assertEquals("none yet", threshold.getPreviousValue());
		assertTrue(threshold.getTimestamp() > 0);
		assertEquals(0, threshold.getFlipCount());


		long oldTime = threshold.getTimestamp();
		threshold.update("1");

		assertEquals(ThresholdStatus.GREEN, threshold.getStatus());
		assertEquals(ThresholdStatus.OFF, threshold.getPreviousStatus());
		assertEquals("1", threshold.getValue());
		assertEquals("none yet", threshold.getPreviousValue());
		assertTrue(threshold.getTimestamp() >= oldTime);
		assertEquals(1, threshold.getFlipCount());


		oldTime = threshold.getTimestamp();
		threshold.update("10");

		assertEquals(ThresholdStatus.GREEN, threshold.getStatus());
		assertEquals(ThresholdStatus.OFF, threshold.getPreviousStatus());
		assertEquals("10", threshold.getValue());
		assertEquals("1", threshold.getPreviousValue());
		assertTrue(threshold.getTimestamp() >= oldTime);
		assertEquals(1, threshold.getFlipCount());


		oldTime = threshold.getTimestamp();
		threshold.update("25");

		assertEquals(ThresholdStatus.ORANGE, threshold.getStatus());
		assertEquals(ThresholdStatus.GREEN, threshold.getPreviousStatus());
		assertEquals("25", threshold.getValue());
		assertEquals("10", threshold.getPreviousValue());
		assertTrue(threshold.getTimestamp() >= oldTime);
		assertEquals(2, threshold.getFlipCount());


		oldTime = threshold.getTimestamp();
		threshold.update("55.5");

		assertEquals(ThresholdStatus.PURPLE, threshold.getStatus());
		assertEquals(ThresholdStatus.ORANGE, threshold.getPreviousStatus());
		assertEquals("55.5", threshold.getValue());
		assertEquals("25", threshold.getPreviousValue());
		assertTrue(threshold.getTimestamp() >= oldTime);
		assertEquals(3, threshold.getFlipCount());


		oldTime = threshold.getTimestamp();
		threshold.update("1");

		assertEquals(ThresholdStatus.GREEN, threshold.getStatus());
		assertEquals(ThresholdStatus.PURPLE, threshold.getPreviousStatus());
		assertEquals("1", threshold.getValue());
		assertEquals("55.5", threshold.getPreviousValue());
		assertTrue(threshold.getTimestamp() >= oldTime);
		assertEquals(4, threshold.getFlipCount());
	}


	@Test
	public void testUpdateHistory() {
		DataThresholdAlertHistory history = DataThresholdAlertHistory.INSTANCE;
		history.clear();
		DataThresholdAlert alert;

		assertEquals(111, history.historySize);    //cogfig file


		assertEquals(ThresholdStatus.OFF, threshold.getStatus());
		assertEquals(ThresholdStatus.OFF, threshold.getPreviousStatus());
		assertTrue(history.getAlerts().isEmpty());


		threshold.update("1");

		assertEquals(ThresholdStatus.GREEN, threshold.getStatus());
		assertEquals(ThresholdStatus.OFF, threshold.getPreviousStatus());

		assertEquals(1, history.getAlerts().size());
		alert = history.getAlerts().get(0);
		assertEquals(threshold, alert.getThreshold());
		assertEquals(ThresholdStatus.GREEN, alert.getStatus());
		assertEquals(ThresholdStatus.OFF, alert.getPreviousStatus());
		assertEquals("1", alert.getValue());
		assertEquals("none yet", alert.getPreviousValue());


		threshold.update("3");

		assertEquals(ThresholdStatus.GREEN, threshold.getStatus());
		assertEquals(ThresholdStatus.OFF, threshold.getPreviousStatus());
		assertEquals(1, history.getAlerts().size());


		threshold.update("11");

		assertEquals(ThresholdStatus.YELLOW, threshold.getStatus());
		assertEquals(ThresholdStatus.GREEN, threshold.getPreviousStatus());
		assertEquals(2, history.getAlerts().size());

		alert = history.getAlerts().get(0);
		assertEquals(threshold, alert.getThreshold());
		assertEquals(ThresholdStatus.YELLOW, alert.getStatus());
		assertEquals(ThresholdStatus.GREEN, alert.getPreviousStatus());
		assertEquals("11", alert.getValue());
		assertEquals("3", alert.getPreviousValue());
	}


	@Test
	public void testUpdateHistoryForTwoThresholds() {
		List<String> lines2 = new LinkedList<>();
		lines2.add("varName2 DOWN 20 GREEN");
		lines2.add("varName2 UP 20 YELLOW");
		lines2.add("varName2 UP 40 ORANGE");
		lines2.add("varName2 UP 60 RED");
		lines2.add("varName2 UP 80 PURPLE");

		String name2 = "varName2";

		DataThreshold threshold2 = new DataThreshold();
		threshold2.configure(name2, lines2);

		DataThresholdAlertHistory history = DataThresholdAlertHistory.INSTANCE;
		history.clear();
		DataThresholdAlert alert;


		threshold.update("1");

		assertEquals(1, history.getAlerts().size());
		alert = history.getAlerts().get(0);
		assertEquals(threshold, alert.getThreshold());


		threshold2.update("35");

		assertEquals(2, history.getAlerts().size());
		alert = history.getAlerts().get(0);
		assertEquals(threshold2, alert.getThreshold());
		assertEquals(ThresholdStatus.YELLOW, alert.getStatus());
		assertEquals(ThresholdStatus.OFF, alert.getPreviousStatus());
		assertEquals("35", alert.getValue());
		assertEquals("none yet", alert.getPreviousValue());


		threshold.update("50");
		threshold2.update("50");

		assertEquals(4, history.getAlerts().size());

		alert = history.getAlerts().get(0);
		assertEquals(threshold2, alert.getThreshold());
		assertEquals(ThresholdStatus.ORANGE, alert.getStatus());
		assertEquals(ThresholdStatus.YELLOW, alert.getPreviousStatus());
		assertEquals("50", alert.getValue());
		assertEquals("35", alert.getPreviousValue());

		alert = history.getAlerts().get(1);
		assertEquals(threshold, alert.getThreshold());
		assertEquals(ThresholdStatus.PURPLE, alert.getStatus());
		assertEquals(ThresholdStatus.GREEN, alert.getPreviousStatus());
		assertEquals("50", alert.getValue());
		assertEquals("1", alert.getPreviousValue());
	}

	@Test
	public void testStatusUpdateHistoryWithThresholds() {
		Application app = new Application("testApp");
		StatusChangeEvent event = new StatusChangeEvent();
		event.setApplication(app);
		event.setStatus(new Status(HealthColor.ORANGE, "10"));
		event.setOldStatus(new Status(HealthColor.GREEN, "1"));
		event.setTimestamp(System.currentTimeMillis());
		event.setComponent(new Component(app, "Name"));
		StatusUpdateHistoryRepository.getInstance().notifyStatusChange(event);

		assertEquals(1, StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("testApp").size());

		StatusUpdateHistoryItem item = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("testApp").get(0);
		assertEquals("Name", item.getComponent().getName());
		assertEquals(HealthColor.ORANGE, item.getNewStatus().getHealth());
		assertEquals(HealthColor.GREEN, item.getOldStatus().getHealth());


		threshold.update("1");

		assertEquals(1, StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("Thresholds").size());
		item = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("Thresholds").get(0);
		assertEquals("varName.THRESHOLD", item.getComponent().getName());
		assertEquals(HealthColor.GREEN, item.getNewStatus().getHealth());
		assertEquals(HealthColor.NONE, item.getOldStatus().getHealth());

		threshold.update("3");

		assertEquals(1, StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("Thresholds").size());


		threshold.update("11");
		threshold.update("41");
		threshold.update("1");

		assertEquals(4, StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("Thresholds").size());
		assertEquals(1, StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("testApp").size());

		item = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("testApp").get(0);
		assertEquals("Name", item.getComponent().getName());
		assertEquals(HealthColor.ORANGE, item.getNewStatus().getHealth());
		assertEquals(HealthColor.GREEN, item.getOldStatus().getHealth());

		item = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("Thresholds").get(0);
		assertEquals("varName.THRESHOLD", item.getComponent().getName());
		assertEquals(HealthColor.GREEN, item.getNewStatus().getHealth());
		assertEquals(HealthColor.PURPLE, item.getOldStatus().getHealth());
	}
}
