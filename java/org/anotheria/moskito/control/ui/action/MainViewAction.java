package org.anotheria.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.anotheria.moskito.control.core.Application;
import org.anotheria.moskito.control.core.ApplicationRepository;
import org.anotheria.moskito.control.core.Component;
import org.anotheria.moskito.control.ui.bean.ApplicationBean;
import org.anotheria.moskito.control.ui.bean.ComponentCountByHealthStatusBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this class
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
		Application current = repository.getApplication(currentApplicationName);
		if (current!=null){
			List<Component> components = current.getComponents();
			for (Component c : components){
				countByStatusBean.addColor(c.getHealthColor());
			}
		}


		httpServletRequest.setAttribute("countByStatus", countByStatusBean);
		return actionMapping.success();
	}
}
