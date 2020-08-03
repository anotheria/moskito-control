package org.moskito.controlagent.endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.04.13 23:26
 */
public class EndpointUtility {
	public static final byte[] object2JSON(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ReplyWrapper wrapper = new ReplyWrapper(obj);
		String jsonOutput = gson.toJson(wrapper);
		return jsonOutput.getBytes();
	}
}
