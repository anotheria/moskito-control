package org.anotheria.moskito.control.ui.bean;

import org.anotheria.moskito.control.core.Component;
import org.anotheria.moskito.control.ui.action.BaseMoSKitoControlAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.04.13 00:29
 */
public class ComponentCountAndStatusByCategoryBean {
	private Map<String, CategoryBean> categoryBeans;
	private CategoryBean all;

	public ComponentCountAndStatusByCategoryBean(){
		categoryBeans = new HashMap<String, CategoryBean>();
		all = new CategoryBean(BaseMoSKitoControlAction.VALUE_ALL_CATEGORIES);
		all.setAll(true);
	}

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
