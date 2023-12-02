package org.moskito.control.core.proxy;

import org.moskito.control.config.ProxyConfig;
import org.moskito.control.core.Component;

public class ProxiedComponent extends Component {

    private ProxyConfig config;

    public ProxiedComponent(String name, ProxyConfig config) {
        super(name);

        this.config = config;
    }

    @Override
    public String getName() {
        return config.getPrefix() + ":" + super.getName();
    }

    public ProxyConfig getConfig() {
        return config;
    }
}
