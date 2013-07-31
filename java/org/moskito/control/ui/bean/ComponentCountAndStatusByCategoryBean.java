package org.moskito.control.ui.bean;

import org.moskito.control.core.Component;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This bean is a container for component count and status sorted by category name.
 *
 * @author lrosenberg
 * @since 02.04.13 00:29
 */
public class ComponentCountAndStatusByCategoryBean {
	/**
	 * Map with category beans sorted by category name.
	 */
	private Map<String, CategoryBean> categoryBeans;
	/**
	 * Bean representing a cumulated 'all' category.
	 */
	private CategoryBean all;

	public ComponentCountAndStatusByCategoryBean(){
		categoryBeans = new HashMap<String, CategoryBean>();
		all = new CategoryBean(BaseMoSKitoControlAction.VALUE_ALL_CATEGORIES);
		all.setAll(true);
	}

	/**
	 * Processes and adds to internal data structures a component.
	 * @param c component to add.
	 */
	public void processComponent(Component c){
		String category = c.getCategory();
		CategoryBean bean = categoryBeans.get(category);
		if (bean==null){
			bean = new CategoryBean(category);
			categoryBeans.put(category, bean);
		}
		bean.processStatus(c.getHealthColor());
		all.processStatus(c.getHealthColor());
	}

	public List<CategoryBean> getCategoryBeans(){
		ArrayList<CategoryBean> ret = new ArrayList<CategoryBean>();
		ret.addAll(categoryBeans.values());
		Collections.sort(ret);
		ret.add(0, all);
		return ret;
	}
}
