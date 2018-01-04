package org.moskito.control.connectors.response;

import java.util.Map;

/**
 * Contains connector information message.
 *
 * @author strel
 */
public class ConnectorInformationResponse extends ConnectorResponse {

	/**
	 * Connector information message as {@link Map}.
	 */
	private Map<String, String> info;


	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}


	@Override
	public String toString() {
		return "ConnectorInformationResponse{" +
				"info=" + info +
				'}';
	}
}
