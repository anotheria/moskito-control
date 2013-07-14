package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.Mocking;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.07.13 13:34
 */
public class AttributeCollectorMocking implements Mocking {

	private Map<String, Object> attributes = new HashMap<String, Object>();

	public void setAttribute(String name, Object o){
		attributes.put(name, o);
	}

	public Object getAttribute(String name){
		return attributes.get(name);
	}
}
