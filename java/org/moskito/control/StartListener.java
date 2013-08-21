package org.moskito.control;

import net.anotheria.util.maven.MavenVersion;
import net.anotheria.webutils.util.VersionUtil;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.core.updater.ApplicationStatusUpdater;
import org.moskito.control.core.updater.ChartDataUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ServletContextListener that ensures that MoSKito-Control components are started in proper order.
 *
 * @author lrosenberg
 * @since 28.05.13 21:15
 */
public class StartListener implements ServletContextListener{

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(StartListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		log.info("Starting up MoSKito Control...");

		ApplicationRepository.getInstance();
		log.info("ApplicationRepository loaded.");

		//initialize history
		StatusUpdateHistoryRepository.getInstance();
		log.info("StatusUpdateHistoryRepository loaded.");

		ApplicationStatusUpdater.getInstance();
		log.info("Application StatusResource Updater loaded.");

		ChartDataUpdater.getInstance();
		log.info("ChartData Updater loaded.");


		String versionString = "unknown";
		try{
			MavenVersion appVersion = VersionUtil.getWebappVersion(servletContextEvent.getServletContext());
			versionString = appVersion.getVersion();
		}catch(Exception e){
			log.warn("couldn't read version.");
		}
		servletContextEvent.getServletContext().setAttribute("moskito.control.version", versionString);
		log.info("MoSKito Control "+versionString+" started.");

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}
