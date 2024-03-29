package org.moskito.control.ui.action;

import jakarta.servlet.http.HttpServletRequest;
import net.anotheria.anoprise.mocking.MockFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.moskito.control.common.AccumulatorDataItem;
import org.moskito.control.core.View;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.ui.bean.ChartBean;
import org.moskito.control.ui.bean.ChartPointBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.07.13 13:31
 */
@RunWith(MockitoJUnitRunner.class)
public class MainViewActionChartsTest {
	@Test public void testEmptyChart(){
		Chart c = new Chart("Chart 1", -1);
		AttributeCollectorMocking map = new AttributeCollectorMocking();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, map);

		View testView = Mockito.mock(View.class);
		when(testView.getCharts()).thenReturn(Arrays.asList(new Chart[]{c}));
		testView.setChartFilter(new String[]{"Chart 1"});

		MainViewAction v = new MainViewAction();
		v.prepareCharts(testView, request);

		List<ChartBean> beans = (List<ChartBean>)map.getAttribute("chartBeans");
		assertNotNull(beans);
		assertEquals(1, beans.size());

		ChartBean first = beans.get(0);
		assertEquals("Chart 1", first.getName());
		assertEquals("Chart_1", first.getDivId());


	}

	@Test public void testOneLine(){
		Chart c = new Chart( "Chart 1", -1);
		c.addLine("comp1", "acc1");
		AttributeCollectorMocking map = new AttributeCollectorMocking();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, map);

		View testView = Mockito.mock(View.class);
		when(testView.getCharts()).thenReturn(Arrays.asList(new Chart[]{c}));
		testView.setChartFilter(new String[]{"Chart 1"});

		//create data
		List<AccumulatorDataItem> data = new ArrayList<AccumulatorDataItem>();
		for (int i=0; i<100; i++){
			data.add(new AccumulatorDataItem(System.currentTimeMillis()+1000L*60*i, ""+i));
		}
		c.notifyNewData("comp1", "acc1", data);



		MainViewAction v = new MainViewAction();
		v.prepareCharts(testView, request);

		List<ChartBean> beans = (List<ChartBean>)map.getAttribute("chartBeans");
		assertNotNull(beans);
		assertEquals(1, beans.size());

		List<ChartPointBean> points = beans.get(0).getPoints();
		assertEquals(100, points.size());
		ensureNumberOfValues(points, 1);

	}


	//this test check that the handling of two lines that are not parallel to each other is proper.
	@Test public void testUnparalleledLines(){
		Chart c = new Chart("Chart 1", -1);
		c.addLine("comp1", "acc1");
		c.addLine("comp2", "acc1");
		AttributeCollectorMocking map = new AttributeCollectorMocking();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, map);

		View testView = Mockito.mock(View.class);
		when(testView.getCharts()).thenReturn(Arrays.asList(new Chart[]{c}));
		testView.setChartFilter(new String[]{"Chart 1"});

		//create data
		List<AccumulatorDataItem> data1 = new ArrayList<AccumulatorDataItem>();
		List<AccumulatorDataItem> data2 = new ArrayList<AccumulatorDataItem>();
		for (int i=0; i<100; i++){
			data1.add(new AccumulatorDataItem(System.currentTimeMillis()+1000L*60*i, ""+i));
			data2.add(new AccumulatorDataItem(System.currentTimeMillis()-1000L*60*i, ""+i));
		}
		c.notifyNewData("comp1", "acc1", data1);
		c.notifyNewData("comp2", "acc1", data2);



		MainViewAction v = new MainViewAction();
		v.prepareCharts(testView, request);

		List<ChartBean> beans = (List<ChartBean>)map.getAttribute("chartBeans");
		assertNotNull(beans);
		assertEquals(1, beans.size());

		List<ChartPointBean> points = beans.get(0).getPoints();
		//we expect 199 points because each line has 100 values, but they only have on common value, at 0.
		assertEquals(100+100-1, points.size());
		ensureNumberOfValues(points, 2);

		assertEquals("0", points.get(0).getValues().get(0));
		assertEquals("99", points.get(0).getValues().get(1));

		//mid value is null,null
		assertEquals("0", points.get(99).getValues().get(0));
		assertEquals("0", points.get(99).getValues().get(1));

		//last 99,0
		assertEquals("99", points.get(points.size()-1).getValues().get(0));
		assertEquals("0", points.get(points.size()-1).getValues().get(1));
	}



	//this test check that the handling of two lines that are not parallel to each other is proper.
	@Test public void testParalleledLines(){
		Chart c = new Chart("Chart 1", -1);
		c.addLine("comp1", "acc1");
		c.addLine("comp2", "acc1");

		View testView = Mockito.mock(View.class);
		when(testView.getCharts()).thenReturn(Arrays.asList(new Chart[]{c}));
		testView.setChartFilter(new String[]{"Chart 1"});

		AttributeCollectorMocking map = new AttributeCollectorMocking();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, map);

		//create data
		List<AccumulatorDataItem> data1 = new ArrayList<AccumulatorDataItem>();
		List<AccumulatorDataItem> data2 = new ArrayList<AccumulatorDataItem>();
		for (int i=0; i<100; i++){
			data1.add(new AccumulatorDataItem(System.currentTimeMillis()+1000L*60*i, ""+i));
			data2.add(new AccumulatorDataItem(System.currentTimeMillis()+1000L*60*i, ""+i*2));
		}
		c.notifyNewData("comp1", "acc1", data1);
		c.notifyNewData("comp2", "acc1", data2);



		MainViewAction v = new MainViewAction();
		v.prepareCharts(testView, request);

		List<ChartBean> beans = (List<ChartBean>)map.getAttribute("chartBeans");
		assertNotNull(beans);
		assertEquals(1, beans.size());

		List<ChartPointBean> points = beans.get(0).getPoints();
		//we expect 199 points because each line has 100 values, but they only have on common value, at 0.
		assertEquals(100, points.size());
		ensureNumberOfValues(points, 2);

		//first value is 0,0
		assertEquals("0", points.get(0).getValues().get(0));
		assertEquals("0", points.get(0).getValues().get(1));

		//second value is 1,2
		assertEquals("1", points.get(1).getValues().get(0));
		assertEquals("2", points.get(1).getValues().get(1));

		//last value is 100-1, (100-1)*2
		assertEquals(""+(100-1), points.get(99).getValues().get(0));
		assertEquals(""+(100-1)*2, points.get(99).getValues().get(1));
	}

	private void ensureNumberOfValues(List<ChartPointBean> points, int valueCount){
		for (ChartPointBean cpb : points){
			assertEquals(valueCount, cpb.getValues().size());
		}
	}
}
