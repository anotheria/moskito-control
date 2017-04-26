package org.moskito.control.plugins;

/**
 * Adapter that allows to implement moskito control plugins by overriding only the needed methods.
 *
 * @author lrosenberg
 */
public abstract class AbstractMoskitoControlPlugin implements MoskitoControlPlugin {
	@Override
	public void setConfigurationName(String configurationName) {
	}

	@Override
	public void initialize() {
	}

	@Override
	public void deInitialize() {
	}
}

