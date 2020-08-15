package org.moskito.control.plugins.pagespeed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.08.20 17:27
 */
public class PagespeedTask {

	/**
	 * Log.
	 */
	private static Logger log = LoggerFactory.getLogger(PagespeedTask.class);
	/**
	 * Config for this task.
	 */
	private PagespeedPluginTargetConfig targetConfig;
	/**
	 * Api key.
	 */
	private String apiKey;

	public PagespeedTask (String apiKey, PagespeedPluginTargetConfig target){
		this.apiKey = apiKey;
		targetConfig = target;
	}

	public Map<String,String> execute() throws IOException {
		String url = "https://www.googleapis.com/pagespeedonline/v5/runPagespeed?";
		if (apiKey!=null && apiKey.length()>0){
			url += "key="+apiKey+"&";
		}
		url += "strategy="+targetConfig.getStrategy()+"&";
		url += "url="+targetConfig.getUrl();

		log.debug("Retrieving URL "+url);

		String content = PagespeedHttpHelper.getURLContent(url);
		// System.out.println("Content: "+content);
		Map<String,String> result = parse(content);
		if (log.isDebugEnabled())
			log.debug("Response "+result);
		return result;
	}

	private static Map<String,String> parse(String content){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		HashMap<String,String> ret = new HashMap<>();

		Map hashMap = gson.fromJson(content, HashMap.class);
		for (String r : Constants.ELEMENTS){
			try {
				String value = retrieve(hashMap, r);
				ret.put(r, value);
			}catch(Exception any){
				log.warn("Couldn't retrieve +r+ from "+hashMap);
			}
		}

		//add metrics.
		Map metrics = (Map)((Map)((Map)hashMap.get("lighthouseResult")).get("audits")).get("metrics");
		metrics = (Map)metrics.get("details");
		List items = (List)metrics.get("items");
		metrics = (Map)items.get(0);

		for (String m : Constants.METRICS){
			String value = ""+metrics.get(m);
			ret.put(m, value);
		}
		return ret;
	}

	private static String retrieve(Map source, String what){
		//System.out.println("retrieving "+what+" from "+source);
		String tokens[] = StringUtils.tokenize(what, '.');
		Map current = source;
		for (int i =0; i<tokens.length-1; i++){
			current = (Map)current.get(tokens[i]);
			if (current==null) {
				System.out.println("In "+what+" not found "+tokens[i]);
				return "";
			}
		}

		return ""+current.get(tokens[tokens.length-1]);

	}

	@Override
	public String toString() {
		return "PagespeedTask{" +
				"targetConfig=" + targetConfig +
				", apiKey='" + apiKey + '\'' +
				'}';
	}

	public String getName(){
		return targetConfig.getName();
	}
}
