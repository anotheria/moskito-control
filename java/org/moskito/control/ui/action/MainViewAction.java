package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.Date;
import net.anotheria.util.NumberUtils;
import net.anotheria.util.StringUtils;
import net.anotheria.util.TimeUnit;
import net.anotheria.util.sorter.DummySortType;
import net.anotheria.util.sorter.StaticQuickSorter;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.accumulator.AccumulatorDataItem;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.chart.ChartLine;
import org.moskito.control.core.Component;
import org.moskito.control.core.threshold.ThresholdDataItem;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.core.notification.StatusChangeMailNotifier;
import org.moskito.control.ui.bean.ApplicationBean;
import org.moskito.control.ui.bean.CategoryBean;
import org.moskito.control.ui.bean.ChartBean;
import org.moskito.control.ui.bean.ChartPointBean;
import org.moskito.control.ui.bean.ComponentBean;
import org.moskito.control.ui.bean.ComponentCountAndStatusByCategoryBean;
import org.moskito.control.ui.bean.ComponentCountByHealthStatusBean;
import org.moskito.control.ui.bean.ComponentHolderBean;
import org.moskito.control.ui.bean.HistoryItemBean;
import org.moskito.control.ui.bean.ReferencePoint;
import org.moskito.control.ui.bean.ThresholdBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This action creates the main view data and redirects to the jsp.
 *
 * @author lrosenberg
 * @since 01.04.13 13:47
 */
public class MainViewAction extends BaseMoSKitoControlAction{

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MainViewAction.class);

	@Override
	public ActionCommand execute(ActionMapping actionMapping, FormBean formBean, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

		ApplicationRepository repository = ApplicationRepository.getInstance();
		List<Application> applications = repository.getApplications();
		ArrayList<ApplicationBean> applicationBeans = new ArrayList<ApplicationBean>();
		String currentApplicationName = getCurrentApplicationName(httpServletRequest);
		if (currentApplicationName==null)
			currentApplicationName = MoskitoControlConfiguration.getConfiguration().getDefaultApplication();
		//if we've got no selected and no default application, lets check if there is only one.
		if (currentApplicationName == null && applications.size()==1){
			currentApplicationName = applications.get(0).getName();
		}
		for (Application app : applications){
			ApplicationBean bean = new ApplicationBean();
			bean.setName(app.getName());
			bean.setColor(app.getWorstHealthStatus().toString().toLowerCase());
			if (app.getName().equals(currentApplicationName))
				bean.setActive(true);
			applicationBeans.add(bean);
		}
		httpServletRequest.setAttribute("applications", applicationBeans);

		ComponentCountByHealthStatusBean countByStatusBean = new ComponentCountByHealthStatusBean();
		ComponentCountAndStatusByCategoryBean countByCategoryBean = new ComponentCountAndStatusByCategoryBean();
		Application current = repository.getApplication(currentApplicationName);
		//add status for tv
		if (current!=null){
			httpServletRequest.setAttribute("tvStatus", current.getWorstHealthStatus().toString().toLowerCase());
		}else{
			httpServletRequest.setAttribute("tvStatus", "none");
		}

		List<CategoryBean> categoryBeans = Collections.emptyList();
		List<ComponentHolderBean> holders = new ArrayList<ComponentHolderBean>();
		String selectedCategory = getCurrentCategoryName(httpServletRequest);
		if (selectedCategory==null)
			selectedCategory = "";

		if (current!=null){
			List<Component> components = current.getComponents();
			for (Component c : components){
				countByCategoryBean.processComponent(c);
			}

			categoryBeans = countByCategoryBean.getCategoryBeans();
			//set all category selected by default
			CategoryBean allCategory = categoryBeans.get(0);
			allCategory.setSelected(true);

			if (selectedCategory!=null && selectedCategory.length()!=0){
				for (CategoryBean cb : categoryBeans){
					if (cb.getName().equals(selectedCategory)){
						allCategory.setSelected(false);
						cb.setSelected(true);
					}
				}
			}

			//preparing component holder.
			Map<String, List<ComponentBean>> componentsByCategories= new HashMap<String, List<ComponentBean>>();
			Map<String, CategoryBean> categoriesByCategoryNames = new HashMap<String, CategoryBean>();
			for (CategoryBean categoryBean : categoryBeans){
				if (!categoryBean.isAll()){
					componentsByCategories.put(categoryBean.getName(), new ArrayList<ComponentBean>());
					categoriesByCategoryNames.put(categoryBean.getName(), categoryBean);
				}
			}

			for (Component c : components){
				if (selectedCategory.length()==0 || selectedCategory.equals(c.getCategory())){
					countByStatusBean.addColor(c.getHealthColor());
					ComponentBean cBean = new ComponentBean();
					cBean.setName(c.getName());
					cBean.setColor(c.getHealthColor().toString().toLowerCase());
                    cBean.setMessages(c.getStatus().getMessages());
					cBean.setUpdateTimestamp(NumberUtils.makeISO8601TimestampString(c.getLastUpdateTimestamp()));
                    List<ThresholdDataItem> tdiList = c.getThresholds();
                    List<ThresholdBean> tBeans = new ArrayList<ThresholdBean>();
                    for (ThresholdDataItem tdi : tdiList) {
                        ThresholdBean tBean = new ThresholdBean();
                        tBean.setName(tdi.getName());
                        tBean.setStatus(tdi.getStatus().toString().toLowerCase());
                        tBean.setLastValue(tdi.getLastValue());
                        tBean.setStatusChangeTimestamp(NumberUtils.makeISO8601TimestampString(tdi.getStatusChangeTimestamp()));
                        tBeans.add(tBean);
                    }
                    cBean.setThresholds(tBeans);
					componentsByCategories.get(c.getCategory()).add(cBean);
				}
			}

			//now finally make component holder beans
			for (Map.Entry<String,List<ComponentBean>> entry : componentsByCategories.entrySet()){
				if (entry.getValue().size()==0)
					continue;
				ComponentHolderBean holderBean = new ComponentHolderBean();
				holderBean.setComponents(entry.getValue());
				holderBean.setCategory(categoriesByCategoryNames.get(entry.getKey()));
				holders.add(holderBean);
			}
		}


		httpServletRequest.setAttribute("countByStatus", countByStatusBean);
		httpServletRequest.setAttribute("categories", categoryBeans);
		httpServletRequest.setAttribute("componentHolders", holders);

		//this call enforces the base class to put the default value if no flag is set yet.
		isStatusOn(httpServletRequest);


		//prepare history
		if (currentApplicationName!=null && currentApplicationName.length()>0 && isHistoryOn(httpServletRequest)){
			List<StatusUpdateHistoryItem> historyItems = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication(currentApplicationName);
			LinkedList<HistoryItemBean> historyItemBeans = new LinkedList<HistoryItemBean>();
			for (StatusUpdateHistoryItem hi : historyItems){

				if (selectedCategory.length()==0 || selectedCategory.equals(hi.getComponent().getCategory())){
					HistoryItemBean bean = new HistoryItemBean();
					bean.setTime(NumberUtils.makeISO8601TimestampString(hi.getTimestamp()));
					bean.setComponentName(hi.getComponent().getName());
					bean.setNewStatus(hi.getNewStatus().getHealth().name().toLowerCase());
					bean.setOldStatus(hi.getOldStatus().getHealth().name().toLowerCase());
					historyItemBeans.add(bean);
				}
			}
			httpServletRequest.setAttribute("historyItems", historyItemBeans);
		}


		//prepare charts
		if (currentApplicationName!=null && currentApplicationName.length()>0 && areChartsOn(httpServletRequest)){
			prepareCharts(current, httpServletRequest);
		}

		//put timestamp.
		String lastRefreshTimestamp = NumberUtils.makeISO8601TimestampString();
		httpServletRequest.setAttribute("lastRefreshTimestamp", lastRefreshTimestamp);

		//put config data
		httpServletRequest.setAttribute("configuration", MoskitoControlConfiguration.getConfiguration());

        //put notifications muting data
        httpServletRequest.setAttribute("notificationsMuted", StatusChangeMailNotifier.getInstance().isMuted());
        httpServletRequest.setAttribute("notificationsMutingTime", MoskitoControlConfiguration.getConfiguration().getNotificationsMutingTime());
        long remainingTime = StatusChangeMailNotifier.getInstance().getRemainingMutingTime();
        httpServletRequest.setAttribute("notificationsRemainingMutingTime", remainingTime <= 0 ? "0" : BigDecimal.valueOf((float) remainingTime / 60000).setScale(1, RoundingMode.UP).toString());

		return actionMapping.success();
	}

	private static void prepareReferenceLineAndAdoptChart(Chart chart){
		List<ChartLine> lines = chart.getLines();
		try{
			//get reference line.
			//detect distance
			long minDistance = Long.MAX_VALUE; long maxDistance = 0;
			List<AccumulatorDataItem> items = lines.get(0).getData();
			long previous = items.get(0).getTimestamp();
			for (int i=1; i<items.size(); i++){
				long distance = items.get(i).getTimestamp()-previous;
				if (distance>maxDistance)
					maxDistance = distance;
				if (distance<minDistance)
					minDistance = distance;
				previous = items.get(i).getTimestamp();
			}

			if (minDistance> TimeUnit.MINUTE.getMillis(2)){
				//we only calculate ref line if the distance is above 2 min.
				ArrayList<ReferencePoint> referenceLine = new ArrayList<ReferencePoint>(items.size());
				for (AccumulatorDataItem item : items){
					referenceLine.add(new ReferencePoint(item.getTimestamp()));
				}

				//now we have to recalculate the other lines.
				for (int i=1; i<lines.size(); i++){
					List<AccumulatorDataItem> linesItems = lines.get(i).getData();
					for (AccumulatorDataItem linesItem : linesItems){
						for (ReferencePoint rp : referenceLine){
							if (rp.isInRange(linesItem.getTimestamp(), minDistance)){
								linesItem.setTimestamp(rp.getTimestamp());
								break;
							}
						}
					}
				}
			}

		}catch(Exception ignored){}
	}

	void prepareCharts(Application current, HttpServletRequest httpServletRequest){
		httpServletRequest.setAttribute("chartBeans", prepareChartData(current));

	}

	public static List<ChartBean> prepareChartData(Application current){
		List<Chart> charts = current.getCharts();
		LinkedList<ChartBean> beans = new LinkedList<ChartBean>();
		for (Chart chart : charts){
			ChartBean bean = new ChartBean();
			bean.setDivId(StringUtils.normalize(chart.getName()));
			bean.setName(chart.getName());
			bean.setLegend(chart.getLegend());

			//build points
			HashMap<String, ChartPointBean> points = new HashMap<String, ChartPointBean>();
			List<ChartLine> lines = chart.getLines();

			prepareReferenceLineAndAdoptChart(chart);


			//first iteration is to determine all captions. second iteration is to fill the data at the proper places.
			//first iteration.
			int lastHour = 0;
			for (ChartLine l1 : lines){
				List<AccumulatorDataItem> items = l1.getData();
				for (AccumulatorDataItem item : items){
					String fdCaption = item.getFullDateCaption();
					ChartPointBean point = points.get(fdCaption);
					Date currentDate = new Date(item.getTimestamp());
					int currentHour = currentDate.getHour();
					if (point==null){
						String captionForThePoint = item.getCaption();
						if (currentHour<lastHour){
							captionForThePoint = NumberUtils.itoa(currentDate.getDay(), 2)+"."+NumberUtils.itoa(currentDate.getMonth(), 2)+" "+captionForThePoint;
						}
						point = new ChartPointBean(captionForThePoint, item.getTimestamp());
						points.put(fdCaption, point);

					}
					lastHour = currentHour;
				}
			}

			//second iteration.
			int currentLineCount = 0;
			int skipCount = 0;
			int presentCount =0;
			for (ChartLine l : lines){
				if (l.getData().size()==0){
					log.warn("Got no data for chart: "+chart.getName()+", line: "+l.getChartCaption()+", remove it from chart");
					continue;
				}
				currentLineCount++;
				bean.addLineName(l.getChartCaption());
				HashSet<String> alreadyDone = new HashSet<String>();
				List<AccumulatorDataItem> items = l.getData();
				for (AccumulatorDataItem item : items){
					String fdCaption = item.getFullDateCaption();
					if (alreadyDone.contains(fdCaption)){
						log.warn("Skipped item " + item + " because it resolves to a already used caption " + fdCaption+" in line "+l+" chart "+chart+(skipCount++));
						continue;
					}
					presentCount++;
					ChartPointBean point = points.get(fdCaption);
					point.addValue(item.getValue());
					alreadyDone.add(fdCaption);
				}
				for (ChartPointBean point : points.values() ){
					point.ensureLength(currentLineCount);
				}
			}
			//System.out.println("BUILT POINTS for chart" + chart.getName() + ": " + points);
			Collection<ChartPointBean> calculatedPoints = points.values ();
			List<ChartPointBean> sortedPoints = StaticQuickSorter.sort(calculatedPoints, new DummySortType());

			if (sortedPoints.size()>0){
				boolean emptyValuesPresent = true;
				//if the chart is not empty we have to make additional checks.
				//now check for spaces.
				int numberOfValues = sortedPoints.get(0).getValues().size();
				while(emptyValuesPresent){
					emptyValuesPresent = false;
					for (int i=0; i<sortedPoints.size(); i++){
						ChartPointBean b = sortedPoints.get(i);
						//lets try to fill out with left value first, if there is no left value, than with right value
						for (int v=0; v<numberOfValues; v++){
							if (b.isEmptyValueAt(v) && chart.getLines().get(v).getData().size()>0){ //the second part of condition ensures that we have values at all.
								//log.warn("empty value found at i,v: "+i+", "+v+", chart: "+chart.getName()+" size: "+chart.getLines().get(v).getData().size()+", "+chart.getLines().get(v).getChartCaption());
								emptyValuesPresent = true;
								if (i==0 || sortedPoints.get(i-1).isEmptyValueAt(v)){
									//try right value
									if (sortedPoints.size()==1){
										//the graph is too small, only one value.
										b.setValueAt(v, "0");
									}else{
										//the graph is at least 2 elements wide, we take right elements for fill out.
										try{
											//last value?
											if (i==sortedPoints.size()-1)
												b.setValueAt(v, sortedPoints.get(i-1).getValueAt(v));
											else
												b.setValueAt(v, sortedPoints.get(i+1).getValueAt(v));
										}catch(Exception e){
											log.warn("unexpected chart problem: "+e.getMessage()+" v: "+v+", i: "+i+" - sortedPoints: "+sortedPoints.size()+" numberOfValues: "+numberOfValues+", chart: "+chart.getName());
										}
									}
								}else{
									b.setValueAt(v, sortedPoints.get(i-1).getValueAt(v));
								}
							}
						}
					}
				}
			}


			bean.setPoints(sortedPoints);

			beans.add(bean);
		}

		return beans;
	}
}
