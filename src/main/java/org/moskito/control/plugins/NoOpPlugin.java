package org.moskito.control.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A noop plugin is used for generic testing of plugin interface.
 *
 * @author lrosenberg
 */
public class NoOpPlugin extends AbstractMoskitoControlPlugin {
	private static final Logger LOGGER = LoggerFactory.getLogger(NoOpPlugin.class);

	@Override
	public void initialize() {
		LOGGER.info(this.getClass().getSimpleName()+" initialized");
	}

	@Override
	public void deInitialize() {
		LOGGER.info(this.getClass().getSimpleName()+" de-initialized");
	}
}
