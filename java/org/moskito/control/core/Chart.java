package org.moskito.control.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 18.06.13 23:01
 */
public class Chart {

	private Application parent;

	private String name;

	private List<ChartLine> lines = new LinkedList<ChartLine>();

	public Chart(Application aParent, String aName){
		name = aName;
		parent = aParent;
	}

	public Application getParent() {
		return parent;
	}

	public void setParent(Application parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addLine(String component, String accumulator) {
		lines.add(new ChartLine(component, accumulator));
	}

	public List<String> getNeededAccumulatorsForComponent(String componentName){
		ArrayList<String> ret = new ArrayList<String>();
		for (ChartLine line : lines){
			if (line.getComponent().equals(componentName))
				ret.add(line.getAccumulator());
		}
		return ret;
	}

	@Override public String toString(){
		return "App: "+getParent().getName()+" Chart: "+getName();
	}
}

