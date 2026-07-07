package org.moskito.controlagent.endpoints.springboot;

import org.moskito.controlagent.Agent;
import org.moskito.controlagent.data.info.SystemInfoProvider;
import org.moskito.controlagent.endpoints.ReplyWrapper;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Spring Boot Actuator variant of the MoSKito Control agent.
 * <p>
 * This is the Spring-idiomatic counterpart of
 * {@code org.moskito.controlagent.endpoints.http.HttpEndpoint}: instead of registering a
 * servlet under {@code /moskito-control-agent/*}, the same
 * data is served as an actuator endpoint under {@code /actuator/moskitocontrol/*}. That means
 * it lands on the management port, participates in Spring Security like any other endpoint,
 * and is enabled the way a Spring developer expects:
 *
 * <pre>management.endpoints.web.exposure.include=moskitocontrol</pre>
 *
 * The wire format is intentionally kept identical to the http-endpoint - every reply is the
 * same {@link ReplyWrapper} envelope ({@code protocolVersion}, {@code timestamp}, {@code reply})
 * around the same {@code org.moskito.controlagent.data.*} beans - so MoSKito Control reads it
 * with the existing {@code HttpConnector} (pointed at this path via {@code agentPath}) and its
 * versioned parsers, with no changes on the collector side.
 *
 * @author lrosenberg
 */
@Endpoint(id = "moskitocontrol")
public class MoskitoControlActuatorEndpoint {

	/**
	 * Supported sub-commands, mirroring {@code HttpEndpoint.COMMAND}.
	 */
	private static final String OP_STATUS = "STATUS";
	private static final String OP_THRESHOLDS = "THRESHOLDS";
	private static final String OP_ACCUMULATORS = "ACCUMULATORS";
	private static final String OP_ACCUMULATOR = "ACCUMULATOR";
	private static final String OP_INFO = "INFO";
	private static final String OP_CONFIG = "CONFIG";
	private static final String OP_NOWRUNNING = "NOWRUNNING";

	/**
	 * Answers {@code GET /actuator/moskitocontrol[/{command}[/{arg}...]]}.
	 * <p>
	 * A single operation captures all remaining path segments, so multi-argument commands work
	 * exactly like the http-endpoint (e.g. {@code /actuator/moskitocontrol/accumulator/Request/Error}),
	 * and the bare path returns a help message. Using one operation (rather than a no-arg plus a
	 * multi-segment one) avoids any ambiguous-mapping surprise at context startup.
	 *
	 * @param tokens command in {@code tokens[0]}, arguments in the rest; empty for the bare path.
	 * @return the wrapped reply, or {@code null} (HTTP 404) for an unknown command.
	 */
	@ReadOperation
	public ReplyWrapper command(@Selector(match = Selector.Match.ALL_REMAINING) String[] tokens) {
		if (tokens == null || tokens.length == 0) {
			return help();
		}

		Agent agent = Agent.getInstance();
		switch (tokens[0].toUpperCase(Locale.ROOT)) {
			case OP_STATUS:
				return new ReplyWrapper(agent.getThresholdStatus());
			case OP_THRESHOLDS:
				return new ReplyWrapper(agent.getThresholds());
			case OP_ACCUMULATORS:
				return new ReplyWrapper(agent.getAvailableAccumulators());
			case OP_ACCUMULATOR:
				return accumulator(agent, tokens);
			case OP_INFO:
				return new ReplyWrapper(SystemInfoProvider.getInstance().getSystemInfo());
			case OP_CONFIG:
				return new ReplyWrapper(agent.getConfig());
			case OP_NOWRUNNING:
				return new ReplyWrapper(agent.getNowRunning());
			default:
				return null;
		}
	}

	private ReplyWrapper help() {
		return new ReplyWrapper("Available commands: " + Arrays.asList(
				OP_STATUS, OP_THRESHOLDS, OP_ACCUMULATORS, OP_ACCUMULATOR, OP_INFO, OP_CONFIG, OP_NOWRUNNING));
	}

	private ReplyWrapper accumulator(Agent agent, String[] tokens) {
		if (tokens.length == 1) {
			throw new IllegalArgumentException("No accumulators specified");
		}
		List<String> names = Arrays.asList(tokens).subList(1, tokens.length);
		return new ReplyWrapper(agent.getAccumulatorsData(names));
	}
}
