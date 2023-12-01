package org.moskito.control.core.proxy;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.config.ProxyConfig;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.core.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProxyConnector {

    private List<View> views = null;
    private Map<String, View> viewByName = new HashMap<>();



    private ProxyConfig config;

    public ProxyConnector(ProxyConfig config) {
        this.config = config;
    }

    public void updateData(){
        System.out.println(this+" updateData()");
        //this method is temporarly, we will make it smarter later.
        //for now, just call the update method of the proxy.
        String content = null;
        try {
            content = HttpHelper.getURLContent(config.getUrl() + "/api/v2/control");
        }catch(Exception any){
            any.printStackTrace();
        }
        System.out.println("Retrieved content: "+content);
        if (content == null)
            return;


        Gson gson = new Gson();
        HashMap<String, Object> map = gson.fromJson(content, HashMap.class);

        Boolean success = (Boolean) map.get("success");
        if (!success) {
            System.out.println("No success....");
            return;
        }

        LinkedTreeMap<String, Object> results = (LinkedTreeMap<String, Object>) map.get("results");
        List<LinkedTreeMap> viewMaps = (List<LinkedTreeMap>) results.get("views");
        LinkedList<View> newViews = new LinkedList<>();
        for (LinkedTreeMap viewMap : viewMaps) {
            System.out.println("View: "+viewMap.get("name"));
            ProxiedView v = new ProxiedView((String) viewMap.get("name"), config);
            v.setProxiedColour(HealthColor.valueOf((String) viewMap.get("viewColor")));

            List<LinkedTreeMap> componentMaps = (List<LinkedTreeMap>) viewMap.get("components");
            for (LinkedTreeMap componentMap : componentMaps) {
                System.out.println("Component: "+componentMap.get("name"));
                ProxiedComponent c = new ProxiedComponent((String) componentMap.get("name"), config);

                HealthColor colour = HealthColor.valueOf((String) componentMap.get("color"));
                List<String> messages = (List<String>) componentMap.get("messages");

                c.setStatus(new Status(colour, messages));

                v.addComponent(c);
            }

            newViews.add(v);
            viewByName.put(v.getName(), v);
        }


        views = newViews;

    }

    public List<View> getViews(){
        return views;
    }

    public View getView(String name) {
        return viewByName.get(name);
    }
}
