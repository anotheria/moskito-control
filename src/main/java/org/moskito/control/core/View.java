package org.moskito.control.core;

import org.moskito.control.core.chart.Chart;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 2019-08-27 14:47
 */
public class View {
	private String name;

	private Set<String> componentCategories = new HashSet<>();
	private Set<String> componentNames = new HashSet<>();
	private Set<String> componentTags = new HashSet<>();
	private Set<String> chartNames = new HashSet<>();
	private Set<String> chartTags = new HashSet<>();

	private Set<String> widgetNames = new HashSet<>();
	private Set<String> widgetTags = new HashSet<>();

	public View(String aName){
		name = aName;
	}

	public String getName(){
		return name;
	}

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
		return valueSet.contains(value);
	}

	private boolean matches(List<String> valueList, Set<String> valueSet){
		for (String value : valueList)
			if (matches(value, valueSet))
				return true;
		return false;
	}

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
			if (matches(w.getTags(), chartTags) ||
					matches(w.getName(), chartNames)){
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
				'}';
	}
}
