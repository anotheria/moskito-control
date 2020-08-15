package org.moskito.control.plugins.pagespeed;

import net.anotheria.util.StringUtils;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.ConnectorType;
import org.moskito.control.config.custom.CustomConfigurationProvider;
import org.moskito.control.config.datarepository.DataProcessingConfig;
import org.moskito.control.config.datarepository.WidgetConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 10.08.20 22:51
 */
public class PagespeedConfigurationProvider implements CustomConfigurationProvider {

	private PagespeedPluginConfig pluginConfig;

	public PagespeedConfigurationProvider(PagespeedPluginConfig aPluginConfig){
		pluginConfig = aPluginConfig;
	}
	
	@Override
	public DataProcessingConfig getDataProcessingConfig() {
		String[] autoWidgets = pluginConfig.getAutoWidgets();
		DataProcessingConfig ret = new DataProcessingConfig();
		if (autoWidgets==null || autoWidgets.length==0)
			return ret;

		PagespeedPluginTargetConfig targets[] = pluginConfig.getTargets();
		if (targets==null || targets.length==0)
			return ret;

		WidgetConfig[] widgets = new WidgetConfig[autoWidgets.length*targets.length];
		int counter=0;
		for (PagespeedPluginTargetConfig target: targets){
			for (String autoW : autoWidgets) {
				String tokens[] = StringUtils.tokenize(autoW, '=');
				WidgetConfig widget = new WidgetConfig();
				widget.setCaption(target.getName()+" "+tokens[0]);
				widget.setType("Number");
				widget.setMapping("number=pagespeed."+target.getName()+"."+tokens[1]);
				widget.setTags("pagespeed");
				widgets[counter++] = widget;
			}
		}
		ret.setWidgets(widgets);
		return ret;
	}

	@Override
	public List<ComponentConfig> getComponents() {
		LinkedList ret = new LinkedList();
		PagespeedPluginTargetConfig targets[] = pluginConfig.getTargets();
		for (PagespeedPluginTargetConfig pptc : targets){
			ComponentConfig componentConfig = new ComponentConfig();
			componentConfig.setCategory(Constants.CATEGORY_NAME);
			componentConfig.setName(Constants.configName2ProducerName(pptc.getName()));
			componentConfig.setTags(Constants.TAGS);
			componentConfig.setConnectorType(ConnectorType.LOCALMOSKITO);
			ret.add(componentConfig);
		}

		return ret;
	}
}
