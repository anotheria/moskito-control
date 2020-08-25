package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.Mocking;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.07.13 00:07
 */
public class HttpSessionMock implements Mocking {
	private Map<String,Object> map = new HashMap<String,Object>();

	public Object getAttribute(String name){
		return map.get(name);
	}

	public void setAttribute(String name, Object o){
		map.put(name, o);
	}

	public void removeAttribute(String name){
		map.remove(name);
	}

}
