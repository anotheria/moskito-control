package org.moskito.control.ui.action;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
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
import org.moskito.control.config.datarepository.RetrieverInstanceConfig;
import org.moskito.control.config.datarepository.VariableMapping;
import org.moskito.control.config.datarepository.WidgetConfig;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.accumulator.AccumulatorDataItem;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.chart.ChartLine;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.data.DataRepository;
import org.moskito.control.data.thresholds.DataThreshold;
import org.moskito.control.data.thresholds.DataThresholdAlert;
import org.moskito.control.data.thresholds.DataThresholdAlertHistory;
import org.moskito.control.ui.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
		if (applications.size()==1){
			currentApplicationName = applications.get(0).getName();
		}
        if (currentApplicationName != null) {
            setCurrentApplicationName(httpServletRequest, currentApplicationName);
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

		ComponentCountByHealthStatusBean countByStatusBean = createStatisticsBeans(httpServletRequest);
		ComponentCountAndStatusByCategoryBean countByCategoryBean = new ComponentCountAndStatusByCategoryBean();

		Application current = repository.getApplication(currentApplicationName);
		httpServletRequest.setAttribute("currentApplication", current);

		//add status for tv
		if (current!=null){
			httpServletRequest.setAttribute("tvStatus", current.getWorstHealthStatus().toString().toLowerCase());
		}else{
			httpServletRequest.setAttribute("tvStatus", "none");
		}

		List<CategoryBean> categoryBeans = Collections.emptyList();
		List<ComponentHolderBean> holders = new ArrayList<ComponentHolderBean>();
		LinkedList<ComponentBean> componentsBeta = new LinkedList<>();

		String selectedCategory = getCurrentCategoryName(httpServletRequest);
		List<HealthColor> selectedStatusFilter = getStatusFilter(httpServletRequest);

		if (current!=null){
			List<Component> components = current.getComponents();
			ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();

			for (Component c : components){
				countByCategoryBean.processComponent(c);
			}

			categoryBeans = countByCategoryBean.getCategoryBeans();
			//set all category selected by default
			CategoryBean allCategory = categoryBeans.get(0);
			allCategory.setSelected(true);

			if (selectedCategory.length()!=0){
				for (CategoryBean cb : categoryBeans){
					if (cb.getName().equals(selectedCategory)){
						allCategory.setSelected(false);
						cb.setSelected(true);
					}
				}
			}

			//preparing component holder.
			Map<String, List<ComponentBean>> filteredComponents = new HashMap<>();
			Map<String, CategoryBean> categoriesByCategoryNames = new HashMap<String, CategoryBean>();

			for (CategoryBean categoryBean : categoryBeans){
				if (!categoryBean.isAll()){
					filteredComponents.put(categoryBean.getName(), new ArrayList<ComponentBean>());
					categoriesByCategoryNames.put(categoryBean.getName(), categoryBean);
				}
			}

			for (Component c : components){
				ComponentBean cBean = new ComponentBean();
				cBean.setName(c.getName());
				cBean.setColor(c.getHealthColor().toString().toLowerCase());
				cBean.setMessages(c.getStatus().getMessages());
				cBean.setUpdateTimestamp(NumberUtils.makeISO8601TimestampString(c.getLastUpdateTimestamp()));
				cBean.setCategoryName(c.getCategory());

				componentsBeta.add(cBean);
				countByStatusBean.addColor(c.getHealthColor());

				// Filtering components by status color and selected category
				if (componentInCategory(c, selectedCategory) && componentHasStatus(c, selectedStatusFilter)) {
					filteredComponents.get(c.getCategory()).add(cBean);
				}
			}

			//now finally make component holder beans
			for (Map.Entry<String,List<ComponentBean>> entry : filteredComponents.entrySet()){
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
		httpServletRequest.setAttribute("componentsBeta", componentsBeta);

		//this call enforces the base class to put the default value if no flag is set yet.
		isStatusOn(httpServletRequest);


		//prepare history
		if (currentApplicationName!=null && currentApplicationName.length()>0 && isHistoryOn(httpServletRequest)){
			List<StatusUpdateHistoryItem> historyItems = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication(currentApplicationName);
			historyItems.addAll(StatusUpdateHistoryRepository.getInstance().getHistoryForApplication("Thresholds"));

			historyItems.sort(new Comparator<StatusUpdateHistoryItem>() {
				@Override
				public int compare(StatusUpdateHistoryItem f, StatusUpdateHistoryItem s) {
					return Long.compare(s.getTimestamp(), f.getTimestamp());
				}
			});


			LinkedList<HistoryItemBean> historyItemBeans = new LinkedList<HistoryItemBean>();
			for (StatusUpdateHistoryItem hi : historyItems){

				if (selectedCategory.length()==0 || selectedCategory.equals(hi.getComponent().getCategory())){
					HistoryItemBean bean = new HistoryItemBean();
					bean.setTime(NumberUtils.makeISO8601TimestampString(hi.getTimestamp()));
					bean.setComponentName(hi.getComponent().getName());
					bean.setNewStatus(hi.getNewStatus().getHealth().name().toLowerCase());
					bean.setOldStatus(hi.getOldStatus().getHealth().name().toLowerCase());
					bean.setMessages(buildThresholdMessageString(hi.getNewStatus().getMessages()));
					historyItemBeans.add(bean);
				}
			}
			httpServletRequest.setAttribute("historyItems", historyItemBeans);
		}


		if (currentApplicationName != null && currentApplicationName.length() > 0 && isDataThresholdsOn(httpServletRequest)) {
			List<DataThresholdBean> dataThresholdsBeans = new LinkedList<>();

			for (DataThreshold dataThreshold : DataRepository.getInstance().getThresholds()) {
				dataThresholdsBeans.add(new DataThresholdBean(dataThreshold));
			}

			httpServletRequest.setAttribute("dataThresholds", dataThresholdsBeans);

			List<DataThresholdAlertBean> alerts = new LinkedList<>();
			for(DataThresholdAlert alert: DataThresholdAlertHistory.INSTANCE.getAlerts()){
				alerts.add(new DataThresholdAlertBean(alert));
			}
			httpServletRequest.setAttribute("dataThresholdsHistory", alerts);
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

		MoskitoControlConfiguration config = MoskitoControlConfiguration.getConfiguration();
		Gson gson = new GsonBuilder().
				setExclusionStrategies(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						return f.getAnnotation(SerializedName.class) == null;
					}

					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						return false;
					}
				}).
				setPrettyPrinting().disableHtmlEscaping().create();

		httpServletRequest.setAttribute("configstring", gson.toJson(config));
		
		Map<String, List<String>> processingMap = new TreeMap<>();
        for (String processingLine : config.getDataprocessing().getProcessing()) {
            String tokens[] = StringUtils.tokenize(processingLine, ' ');
            if (tokens.length > 1) {
                if (!processingMap.containsKey(tokens[1])) {
                    processingMap.put(tokens[1], new LinkedList<>());
                }
                processingMap.get(tokens[1]).add(processingLine);
            }
        }

        Map<String, String> retrieverVariableNames = new HashMap<>();

        for (RetrieverInstanceConfig retriever : config.getDataprocessing().getRetrievers()) {
            for (VariableMapping mapping : retriever.getMappings()) {
                retrieverVariableNames.put(mapping.getVariableName(), mapping.getExpression());
            }
        }

        for (String key : DataRepository.getInstance().getData().keySet()) {
            if (!processingMap.containsKey(key)) {
                processingMap.put(key, new LinkedList<>());
                processingMap.get(key).add(retrieverVariableNames.get(key));
            }
        }

        httpServletRequest.setAttribute("processing", processingMap);
        httpServletRequest.setAttribute("processingData", DataRepository.getInstance().getData());

        //put notifications muting data
        httpServletRequest.setAttribute("notificationsMuted", ApplicationRepository.getInstance().getEventsDispatcher().isMuted());
        httpServletRequest.setAttribute("notificationsMutingTime", MoskitoControlConfiguration.getConfiguration().getNotificationsMutingTime());
        long remainingTime = ApplicationRepository.getInstance().getEventsDispatcher().getRemainingMutingTime();
        httpServletRequest.setAttribute("notificationsRemainingMutingTime", remainingTime <= 0 ? "0" : BigDecimal.valueOf((float) remainingTime / 60000).setScale(1, RoundingMode.UP).toString());


        //data processing
		String[] coniguredWidgetsForThisView = current.getWidgets();
		if (coniguredWidgetsForThisView!=null && coniguredWidgetsForThisView.length>0) {
			List<DataWidgetBean> widgetBeans = new LinkedList<>();
			Map<String, String> data = DataRepository.getInstance().getData();
			for (String configuredWidget : coniguredWidgetsForThisView){
				if (configuredWidget.equals("*")){
					//add all
					WidgetConfig[] widgetsConfigs = MoskitoControlConfiguration.getConfiguration().getDataprocessing().getWidgets();
					if (widgetsConfigs != null) {
						for (WidgetConfig widgetConfig : widgetsConfigs) {
							DataWidgetBean widgetBean = new DataWidgetBean();
							widgetBean.setCaption(widgetConfig.getCaption());
							widgetBean.setType(widgetConfig.getType());

							Map<String, String> mappings = widgetConfig.getMappings();
							for (Map.Entry<String, String> mapping : mappings.entrySet()) {
								String key = mapping.getKey();
								String variable = mapping.getValue();
								widgetBean.addData(key, data.get(variable));
							}


							widgetBeans.add(widgetBean);
						}
					}
				}else{
					//add single
					WidgetConfig widgetConfig = MoskitoControlConfiguration.getConfiguration().getDataprocessing().getWidget(configuredWidget);
					DataWidgetBean widgetBean = new DataWidgetBean();
					widgetBean.setCaption(widgetConfig.getCaption());
					widgetBean.setType(widgetConfig.getType());

					Map<String, String> mappings = widgetConfig.getMappings();
					for (Map.Entry<String, String> mapping : mappings.entrySet()) {
						String key = mapping.getKey();
						String variable = mapping.getValue();
						widgetBean.addData(key, data.get(variable));
					}


					widgetBeans.add(widgetBean);
				}
			}


			httpServletRequest.setAttribute("dataWidgets", widgetBeans);
		}

		return actionMapping.success();
	}

	private String buildThresholdMessageString(List<String> messages){
		if (messages == null || messages.size()==0)
			return "";
		StringBuilder ret = new StringBuilder();
		for (String m : messages){
			if (ret.length()>0)
				ret.append(", ");
			ret.append(m);
		}

		return ret.toString();
	}

	private boolean componentInCategory(Component c, String categoryFilter) {
		return StringUtils.isEmpty(categoryFilter) || categoryFilter.equals(c.getCategory());
	}

	private boolean componentHasStatus(Component c, List<HealthColor> colors) {
		if (colors.isEmpty())
			return true;

		for (HealthColor color : colors) {
			if (color.equals(c.getHealthColor()))
				return true;
		}

		return false;
	}

	private ComponentCountByHealthStatusBean createStatisticsBeans(HttpServletRequest req) {
		ComponentCountByHealthStatusBean statusBeans = new ComponentCountByHealthStatusBean();

		for (HealthColor selectedStatus : getStatusFilter(req)) {
			statusBeans.setSelected(selectedStatus);
		}

		return statusBeans;
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

		}catch(RuntimeException ignored){}
	}

	void prepareCharts(Application current, HttpServletRequest httpServletRequest){
		try{
			httpServletRequest.setAttribute("chartBeans", prepareChartData(current));
		}catch(Exception e){
			log.error("Couldn't prepare chart data, e");
			httpServletRequest.setAttribute("chartBeans", Collections.EMPTY_LIST);
		}

	}

    public static List<ChartBean> prepareChartData(Application current) {
        if (current == null)
            return Collections.EMPTY_LIST;
        return prepareChartData(current.getCharts());
    }

	public static List<ChartBean> prepareChartData(List<Chart> charts){
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
