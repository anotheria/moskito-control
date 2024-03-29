package org.moskito.control.ui.restapi;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

/**
 * ReplyObject is a holder object for all rest api replies. It contains some status info and additional objects
 * with requested information.
 * This is basic reply object for version 2 of the api.
 *
 * @author lrosenberg
 * @since 27.10.2020
 */
@XmlRootElement(name="reply")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(name = "ReplyObject", description = "General reply object for all rest api calls.")
public class ReplyObject {
	/**
	 * True if the call was successful.
	 */
	@XmlElement
	@Schema(description = "True if the call was successful, otherwise the results can be ignored.")
	private boolean success;
	/**
	 * Optional message in case call failed (exception message).
	 */
	@XmlElement(required = false,nillable = false)
	@Schema(description = "Server side message, usually if success is false, can contain underlying exception message.")
	private String message;

	/**
	 * Map with results object.
	 */
	@XmlElement
	@Schema(description = "Map with results objects. Results are operation specific. Some operations can return multiple results.")
	private HashMap<String, Object> results = new HashMap<>();

	/**
	 * Creates a new empty result object.
	 */
	public ReplyObject(){

	}

	/**
	 * Creates a new result object with one result.
	 * @param name name of the result bean.
	 * @param result object for the first result bean.
	 */
	public ReplyObject(String name, Object result){
		results.put(name, result);
	}

	/**
	 * Adds
	 * @param name
	 * @param result
	 */
	public void addResult(String name, Object result){
		results.put(name, result);
	}

	/**
	 * Factory method that creates a new reply object for successful request.
	 * @param name
	 * @param result
	 * @return
	 */
	public static ReplyObject success(String name, Object result){
		ReplyObject ret = new ReplyObject(name, result);
		ret.success = true;
		return ret;
	}

	/**
	 * Factory method that creates a new reply object for successful request.
	 * @return
	 */
	public static ReplyObject success(){
		ReplyObject ret = new ReplyObject();
		ret.success = true;
		return ret;
	}

	/**
	 * Factory method that creates a new erroneous reply object.
	 * @param message
	 * @return
	 */
	public static ReplyObject error(String message){
		ReplyObject ret = new ReplyObject();
		ret.success = false;
		ret.message = message;
		return ret;
	}

	public static ReplyObject error(Throwable exc){
		ReplyObject ret = new ReplyObject();
		ret.success = false;
		ret.message = exc.getClass().getSimpleName()+": "+exc.getMessage();
		return ret;
	}

	@Override public String toString(){
		StringBuilder ret = new StringBuilder("ReplyObject ");
		ret.append("Success: ").append(success);
		if (message!=null){
			ret.append(", Message: ").append(message);
		}
		ret.append(", Results: ").append(results);
		return ret.toString();
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public HashMap getResults(){
		return results;
	}


}
