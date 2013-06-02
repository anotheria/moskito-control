package org.anotheria.moskito.control.connectors;

import org.anotheria.moskito.control.config.ConnectorConfig;
import org.anotheria.moskito.control.config.MoskitoControlConfiguration;
import org.apache.log4j.Logger;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.06.13 23:23
 */
public final class ConnectorFactory {

	private static Logger log = Logger.getLogger(ConnectorFactory.class);

	public static final Connector createConnector(ConnectorType type){
		ConnectorConfig[] connectors = MoskitoControlConfiguration.getConfiguration().getConnectors();
		for (ConnectorConfig c : connectors){
			if (c.getType()==type){
				String className = c.getClassName();
				try{
					Connector connector = (Connector) Class.forName(className).newInstance();
					return connector;
				}catch(ClassNotFoundException e){
					log.error("createConnector("+type+")", e);
					throw new IllegalArgumentException("Can't create connector of type "+type,e);
				} catch (InstantiationException e) {
					log.error("createConnector("+type+")", e);
					throw new IllegalArgumentException("Can't create connector of type "+type,e);
				} catch (IllegalAccessException e) {
					log.error("createConnector("+type+")", e);
					throw new IllegalArgumentException("Can't create connector of type "+type,e);
				}
			}

		}
		throw new IllegalArgumentException(type+" doesn't seem to be supported");
	}
}
