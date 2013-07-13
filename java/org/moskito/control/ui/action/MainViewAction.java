package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.NumberUtils;
import net.anotheria.util.StringUtils;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Chart;
import org.moskito.control.core.Component;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.ui.bean.ApplicationBean;
import org.moskito.control.ui.bean.CategoryBean;
import org.moskito.control.ui.bean.ChartBean;
import org.moskito.control.ui.bean.ComponentBean;
import org.moskito.control.ui.bean.ComponentCountAndStatusByCategoryBean;
import org.moskito.control.ui.bean.ComponentCountByHealthStatusBean;
import org.moskito.control.ui.bean.ComponentHolderBean;
import org.moskito.control.ui.bean.HistoryItemBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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


	@Override
	public ActionCommand execute(ActionMapping actionMapping, FormBean formBean, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

		ApplicationRepository repository = ApplicationRepository.getInstance();
		List<Application> applications = repository.getApplications();
		ArrayList<ApplicationBean> applicationBeans = new ArrayList<ApplicationBean>();
		String currentApplicationName = getCurrentApplicationName(httpServletRequest);
		if (currentApplicationName==null)
			currentApplicationName = "";
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
		List<CategoryBean> categoryBeans = Collections.emptyList();
		List<ComponentHolderBean> holders = new ArrayList<ComponentHolderBean>();
		if (current!=null){
			String selectedCategory = getCurrentCategoryName(httpServletRequest);
			if (selectedCategory==null)
				selectedCategory = "";

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
				HistoryItemBean bean = new HistoryItemBean();
				bean.setTime(NumberUtils.makeISO8601TimestampString(hi.getTimestamp()));
				bean.setComponentName(hi.getComponent().getName());
				bean.setNewStatus(hi.getNewStatus().getHealth().name().toLowerCase());
				bean.setOldStatus(hi.getOldStatus().getHealth().name().toLowerCase());
				historyItemBeans.add(bean);
			}
			httpServletRequest.setAttribute("historyItems", historyItemBeans);
		}


		//prepare charts
		if (currentApplicationName!=null && currentApplicationName.length()>0 && areChartsOn(httpServletRequest)){
			List<Chart> charts = current.getCharts();
			LinkedList<ChartBean> beans = new LinkedList<ChartBean>();
			for (Chart chart : charts){
				ChartBean bean = new ChartBean();
				bean.setDivId(StringUtils.normalize(chart.getName()));
				bean.setName(chart.getName());

				beans.add(bean);
			}

			httpServletRequest.setAttribute("chartBeans", beans);

		}

		return actionMapping.success();
	}
}
