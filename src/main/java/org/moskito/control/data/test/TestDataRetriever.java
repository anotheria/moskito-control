package org.moskito.control.data.test;

import org.moskito.control.data.DataRetriever;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 16:09
 */
public class TestDataRetriever implements DataRetriever{

	private Random rnd = new Random(System.nanoTime());

	@Override
	public Map<String,String> retrieveData() {

		int a = rnd.nextInt(20);
		int b = rnd.nextInt(30);

		HashMap<String, String> ret = new HashMap<>();
		ret.put("test.a", ""+a);
		ret.put("test.b", ""+b);

		return ret;
	}
}
