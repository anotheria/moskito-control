package org.moskito.controlagent.endpoints.http;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.anotheria.moskito.core.config.MoskitoConfiguration;
import net.anotheria.util.StringUtils;
import org.moskito.controlagent.Agent;
import org.moskito.controlagent.data.accumulator.AccumulatorHolder;
import org.moskito.controlagent.data.accumulator.AccumulatorListItem;
import org.moskito.controlagent.data.info.SystemInfo;
import org.moskito.controlagent.data.info.SystemInfoProvider;
import org.moskito.controlagent.data.nowrunning.EntryPoint;
import org.moskito.controlagent.data.status.StatusHolder;
import org.moskito.controlagent.data.threshold.ThresholdDataItem;
import org.moskito.controlagent.endpoints.EndpointUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Provides data replying to MoSKito Control HTTP-requests.
 *
 * @author lrosenberg
 * @since 15.04.13 20:43
 */
@WebFilter(description = "MoSKito Control Endpoint", filterName = "MoSKitoControlEndpoint",
		urlPatterns = {
		"/moskito-control-agent/*"
	})
public class HttpEndpoint implements Filter {

	private static Logger log = LoggerFactory.getLogger(HttpEndpoint.class);

	public static final String VERSION = "3.0.1";

	enum COMMAND{
		/**
		 * Requests this application info
		 */
		INFO,
		/**
		 * Requests the status of this component. The status is calculated based on worst threshold.
		 */
		STATUS,
		/**
		 * Requests the list of the accumulators.
		 */
		ACCUMULATORS,
		/**
		 * Requests the data for one or multiple accumulators.
		 */
		ACCUMULATOR,
        /**
         * Requests the data for thresholds of this component.
         */
        THRESHOLDS,
		/**
		 * Prints a help message
		 */
		HELP,
		/**
		 * Requests moskito config.
		 */
		CONFIG,
		/**
		 * Requests now running.
		 */
		NOWRUNNING
	};

	public static final String MAPPED_NAME = "moskito-control-agent";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String requestURI = request.getRequestURI();
		//we ignore the case that request uri could be null
		String myPath = requestURI.substring(requestURI.indexOf(MAPPED_NAME) +MAPPED_NAME.length() + 1);
		String tokens[] = StringUtils.tokenize(myPath, '/');
		COMMAND command = COMMAND.valueOf(tokens[0].toUpperCase());

		switch(command){
			case STATUS:
				status(request, response, tokens);
				break;
			case ACCUMULATORS:
				accumulators(request, response, tokens);
				break;
			case ACCUMULATOR:
				accumulator(request, response, tokens);
				break;
            case THRESHOLDS:
                thresholds(request, response, tokens);
                break;
			case INFO:
				info(request, response, tokens);
				break;
			case HELP:
				help(request, response, tokens);
				break;
			case CONFIG:
				config(request, response, tokens);
				break;
			case NOWRUNNING:
				nowrunning(request, response, tokens);
				break;

			default:
				throw new AssertionError("Unrecognized command "+command+", try HELP");
		}
	}

	@SuppressFBWarnings(
			value="UnusedLocalVariable",
			justification="We want all methods to have same signature")
    private void status(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String parameters[]) throws IOException{
        StatusHolder status = Agent.getInstance().getThresholdStatus();
        writeReply(servletResponse, status);

    }

    private void accumulators(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String parameters[]) throws IOException{
		List<AccumulatorListItem> ret = Agent.getInstance().getAvailableAccumulators();
		writeReply(servletResponse, ret);
	}

	private void accumulator(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String parameters[]) throws IOException{
		if (parameters.length==1)
			throw new IllegalArgumentException("No accumulators specified");
		LinkedList<String> names = new LinkedList<String>();
		for (int i=1; i<parameters.length; i++){
			names.add(parameters[i]);
		}
		Map<String, AccumulatorHolder> accumulators = Agent.getInstance().getAccumulatorsData(names);
		writeReply(servletResponse, accumulators);
	}

    private void thresholds(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String parameters[]) throws IOException {
        List<ThresholdDataItem> thresholds = Agent.getInstance().getThresholds();
        writeReply(servletResponse, thresholds);
    }

	private void info(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String parameters[]) throws IOException {
		SystemInfo info = SystemInfoProvider.getInstance().getSystemInfo();
		writeReply(servletResponse, info);
	}

	private void help(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String parameters[]) throws IOException {
		StringBuilder reply = new StringBuilder("Available commands: ").append(Arrays.toString(COMMAND.values())).append(", ");
		reply.append("my version is at least "+VERSION);
		writeReply(servletResponse, reply);
	}

	private void config(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String[] tokens) throws IOException {
		MoskitoConfiguration config = Agent.getInstance().getConfig();
		writeReply(servletResponse, config);
	}

	private void nowrunning(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String[] tokens) throws IOException {
		List<EntryPoint> ep =  Agent.getInstance().getNowRunning();
		writeReply(servletResponse, ep);
	}

	void writeReply(HttpServletResponse servletResponse, Object parameter) throws IOException{
		servletResponse.setContentType("application/json");
		servletResponse.addHeader("moskitocontrol.version", VERSION);


		byte[] data = EndpointUtility.object2JSON(parameter);
		OutputStream out = servletResponse.getOutputStream();
		out.write(data);
		out.flush();
		out.close();
	}

	@Override
	public void destroy() {
	}

}
