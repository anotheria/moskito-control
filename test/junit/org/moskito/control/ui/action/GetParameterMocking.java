package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.Mocking;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.07.13 00:00
 */
public class GetParameterMocking implements Mocking{

	private String name ;
	private String value;

	public GetParameterMocking(String name, String value){
		this.name = name;
		this.value = value;

	}

	public String getParameter(String name){
		if (this.name.equals(name))
			return value;
		return "";
	}
}
