package org.moskito.control.ui.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.07.13 01:50
 */
public class ChartPointBean {
	private String caption;
	private List<String> values = new ArrayList<String>();

	public ChartPointBean(String aCaption){
		caption = aCaption;
	}

	public void addValue(String value){
		values.add(value);
	}

	public String toString(){
		return caption+": "+values;
	}

	public List<String> getValues(){
		return values;
	}

	public void ensureLength(int currentLineCount) {
		while(values.size()<currentLineCount)
			values.add("0");
	}
}
