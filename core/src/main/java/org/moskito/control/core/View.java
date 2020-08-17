package org.moskito.control.core;

import org.moskito.control.common.HealthColor;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents a single view in the moskito-control. This is similar concept as was previously part of moskito-control
 * as application. Difference is that multiple views can present same component or chart.
 *
 * @author lrosenberg
 * @since 2019-08-27 14:47
 */
public class View implements Comparable<View>{

	/**
	 * Name of the view.
	 */
	private String name;

	/**
	 * Categories of components that are part of this view.
	 */
	private Set<String> componentCategories = new HashSet<>();
	/**
	 * Names of components that are part of this view.
	 */
	private Set<String> componentNames = new HashSet<>();
	/**
	 * Tags of components that are part of this view.
	 */
	private Set<String> componentTags = new HashSet<>();
	/**
	 * Names of charts that are part of this view.
	 */
	private Set<String> chartNames = new HashSet<>();
	/**
	 * Tags of charts that are part of this view.
	 */
	private Set<String> chartTags = new HashSet<>();

	/**
	 * Names of widgets that are part of this view.
	 */
	private Set<String> widgetNames = new HashSet<>();
	/**
	 * Tags of widgets that are part of this view.
	 */
	private Set<String> widgetTags = new HashSet<>();

	/**
	 * Creates a new view with given name. Name is used to select a view and to present the view in the top navigation.
	 * @param aName
	 */
	public View(String aName){
		name = aName;
	}

	public String getName(){
		return name;
	}

	/**
	 * Returns filtered list of components that should be presented in this view.
	 * @return
	 */
	public List<Component> getComponents() {
		List<Component> allComponents = ComponentRepository.getInstance().getComponents();

		boolean categoryFilter = componentCategories!=null && componentCategories.size()>0;
		boolean nameFilter = componentNames!=null && componentNames.size()>0;
		if (!categoryFilter && !nameFilter)
			return allComponents;

		List<Component> ret = new LinkedList<>();
		for (Component c : allComponents){
			if (matches(c.getCategory(), componentCategories) ||
				matches(c.getName(), componentNames)){
				ret.add(c);

			}
		}
		return ret;
	}

	private boolean matches(String value, Set<String> valueSet){
		//later this will be refactored to support more complicated queries.
		if (valueSet.contains("*"))
			return true;
		return valueSet.contains(value);
	}

	private boolean matches(List<String> valueList, Set<String> valueSet){
		for (String value : valueList)
			if (matches(value, valueSet))
				return true;
		return false;
	}

	/**
	 * Returns the status of the view (color).
	 * @return
	 */
	public HealthColor getWorstHealthStatus() {
		return ComponentRepository.getInstance().getWorstHealthStatus(getComponents());
	}

	public List<Chart> getCharts() {
		List<Chart> allCharts = ComponentRepository.getInstance().getCharts();
		boolean nameFilter = chartNames != null && chartNames.size()>0;
		boolean tagFilter = chartTags != null && chartTags.size()>0;

		if (!tagFilter && !nameFilter)
			return allCharts;

		List<Chart> ret = new LinkedList<>();
		for (Chart c : allCharts){
			if (matches(c.getTags(), chartTags) ||
					matches(c.getName(), chartNames)){
				ret.add(c);

			}
		}

		return ret;
	}

	public List<DataWidget> getDataWidgets() {
		List<DataWidget> allWidgets = ComponentRepository.getInstance().getDataWidgets();
		boolean nameFilter = widgetNames != null && widgetNames.size()>0;
		boolean tagFilter = widgetTags != null && widgetTags.size()>0;

		if (!tagFilter && !nameFilter)
			return allWidgets;

		List<DataWidget> ret = new LinkedList<>();
		for (DataWidget w : allWidgets){
			if (matches(w.getTags(), widgetTags) ||
					matches(w.getName(), widgetNames)){
				ret.add(w);

			}
		}

		return ret;
	}


	public void setComponentCategoryFilter(String[] someComponentCategories) {
		addArrayToHashSet(someComponentCategories, componentCategories);
	}

	public void setComponentFilter(String[] someComponentNames) {
		addArrayToHashSet(someComponentNames, componentNames);
	}

	public void setComponentTagsFilter(String[] someComponentTags) {
		addArrayToHashSet(someComponentTags, componentTags);
	}

	public void setChartFilter(String[] someChartNames) {
		addArrayToHashSet(someChartNames, chartNames);
	}

	public void setChartTagsFilter(String[] someChartTags) {
		addArrayToHashSet(someChartTags, chartTags);
	}

	public void setWidgetsFilter(String[] someWidgetNames) {
		addArrayToHashSet(someWidgetNames, widgetNames);
	}

	public void setWidgetTagsFilter(String[] someWidgetTags) {
		addArrayToHashSet(someWidgetTags, widgetTags);
	}

	private void addArrayToHashSet(String[] array, Set<String> set){
		if (array == null || array.length==0)
			return;
		for (String s : array)
			set.add(s);
	}

	@Override
	public String toString() {
		return "View{" +
				"name='" + name + '\'' +
				", componentCategories=" + componentCategories +
				", componentNames=" + componentNames +
				", componentTags=" + componentTags +
				", chartNames=" + chartNames +
				", chartTags=" + chartTags +
				", widgetNames=" + widgetNames +
				", widgetTags=" + widgetTags +
				'}';
	}

	@Override
	public int compareTo(View o) {
		return name.compareTo(o.getName());
	}

	public List<StatusUpdateHistoryItem> getViewHistory() {
		return StatusUpdateHistoryRepository.getInstance().getHistoryForComponents(getComponents());
	}
}
