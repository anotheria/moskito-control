package org.moskito.control.ui.action.configuration;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO comment this class
 *
 * @author sstreltsov
 */
public class ShowMoskitoControlConfigAction extends BaseMoSKitoControlAction {


	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception{

		MoskitoControlConfiguration config = MoskitoControlConfiguration.getConfiguration();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonOutput = gson.toJson(config);

		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonOutput);
		String prettyJsonString = gson.toJson(je);

		req.setAttribute("configJson", prettyJsonString);

		return mapping.success();
	}
}
