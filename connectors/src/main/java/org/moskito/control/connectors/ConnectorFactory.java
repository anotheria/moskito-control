package org.moskito.control.connectors;

import org.moskito.control.config.ConnectorConfig;
import org.moskito.control.config.ConnectorType;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.connectors.local.LocalMoSKitoConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creation of connector objects. This factory uses the ConnectorConfig entries from the configuration
 * to create Connector objects for different type of connectors. It reacts to configuration changes instantly.
 * New in 3.x - the standard connector are now supported ootb and not needed to be configured. You can still override the
 * standard connectors if you want to, but in 99% of the usages you won't need it.
 *
 * @author lrosenberg
 * @since 02.06.13 23:23
 */
public final class ConnectorFactory {

	/**
	 * Log.
	 */
	private static Logger log = LoggerFactory.getLogger(ConnectorFactory.class);

	/**
	 * Creates a new connector for given type.
	 * @param type type of the connector.
	 * @return the newly created connector object.
	 */
	public static Connector createConnector(ConnectorType type){
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
		return createBuiltInConnector(type);
	}

	private static Connector createBuiltInConnector(ConnectorType type){
		switch (type){
			case RMI:
			case DISTRIBUTEME:
				return new RMIConnector();
			case HTTP:
				return new HttpConnector();
			case URL:
				return new HttpURLConnector();
			case MONGO:
				return new MongoConnector();
			case LOCALMOSKITO:
				return new LocalMoSKitoConnector();
			case JDBC:
				return new JDBCConnector();
			case CONTROL:
				return new MoSKitoControlConnector();
		}
		throw new IllegalArgumentException(type+" doesn't seem to be supported");
	}
}
