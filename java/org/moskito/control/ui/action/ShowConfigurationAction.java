package org.moskito.control.ui.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.moskito.control.config.MoskitoControlConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 18.06.13 13:36
 */
public class ShowConfigurationAction extends BaseMoSKitoControlAction{


	@Override
	public ActionCommand execute(ActionMapping actionMapping, FormBean formBean, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonOutput = gson.toJson(MoskitoControlConfiguration.getConfiguration());

		OutputStream out = httpServletResponse.getOutputStream();
		out.write(jsonOutput.getBytes(Charset.forName("UTF-8")));
		out.flush();
		out.close();
		return null;
	}
}
