package org.anotheria.moskito.control.ui;

import net.anotheria.maf.MAFFilter;
import net.anotheria.maf.action.ActionMappingsConfigurator;

import java.util.Arrays;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 01.04.13 13:54
 */
public class MoSKitoControlFilter extends MAFFilter {

	@Override
	protected List<ActionMappingsConfigurator> getConfigurators() {
		return Arrays.asList(new ActionMappingsConfigurator[]{new MoSKitoControlMappingsConfigurator()});
	}

	@Override
	protected String getDefaultActionName() {
		return "main";
	}
}
