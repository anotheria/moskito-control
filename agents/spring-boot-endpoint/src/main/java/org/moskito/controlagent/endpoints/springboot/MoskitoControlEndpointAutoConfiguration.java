package org.moskito.controlagent.endpoints.springboot;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Registers the {@link MoskitoControlActuatorEndpoint} automatically once this starter is on the
 * classpath of a Spring Boot application. Nothing else is required from the application beyond
 * exposing the endpoint, e.g.:
 *
 * <pre>management.endpoints.web.exposure.include=moskitocontrol</pre>
 *
 * Registered via {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}.
 *
 * @author lrosenberg
 */
@AutoConfiguration
@ConditionalOnClass(Endpoint.class)
public class MoskitoControlEndpointAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MoskitoControlActuatorEndpoint moskitoControlActuatorEndpoint() {
		return new MoskitoControlActuatorEndpoint();
	}
}
