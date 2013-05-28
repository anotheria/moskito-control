package org.anotheria.moskito.control;

import org.anotheria.moskito.control.core.ApplicationRepository;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.05.13 21:15
 */
public class StartListener implements ServletContextListener{

	private static Logger log = Logger.getLogger(StartListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		log.info("Starting up MoSKito Control...");

		ApplicationRepository.getInstance();
		log.info("ApplicationRepository loaded.");

		log.info("MoSKito Control started.");

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}
