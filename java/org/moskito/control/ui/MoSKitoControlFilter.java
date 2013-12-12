package org.moskito.control.ui;

import net.anotheria.maf.MAFFilter;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.util.maven.MavenVersion;
import net.anotheria.webutils.util.VersionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.List;

/**
 * MAF entry point for MoSKito control.
 *
 * @author lrosenberg
 * @since 01.04.13 13:54
 */
public class MoSKitoControlFilter extends MAFFilter {

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MoSKitoControlFilter.class);

	@Override
	protected List<ActionMappingsConfigurator> getConfigurators() {
		return Arrays.asList(new ActionMappingsConfigurator[]{new MoSKitoControlMappingsConfigurator()});
	}

	@Override
	protected String getDefaultActionName() {
		return "main";
	}

	@Override public void init(FilterConfig config) throws ServletException {
		super.init(config);

		try{
			MavenVersion appVersion = VersionUtil.getWebappVersion(config.getServletContext());
			config.getServletContext().setAttribute("application.version_string", appVersion == null ? "unknown" : appVersion.getVersion());
		}catch(Exception e){
			log.error("init("+config+")", e);
		}
	}
}
