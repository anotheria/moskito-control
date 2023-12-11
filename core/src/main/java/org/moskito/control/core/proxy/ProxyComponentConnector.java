package org.moskito.control.core.proxy;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.common.ThresholdDataItem;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.ProxyConfig;
import org.moskito.control.connectors.AbstractConnector;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.connectors.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * ProxyComponentConnector is used to retrieve component information from a different MoSKito Control instance.
 * It only works in cooperation with a ProxiedConnector, meaning that the ProxiedConnector is responsible for providing the view data, and
 * this connector serves for component inspection to retrieve thresholds and accumulators data.
 * Its convenient to have this connector implement Connector interface, so nothing in the rendering code has to be changed.
 */
public class ProxyComponentConnector extends AbstractConnector implements Connector {

    /**
     * Configuration of the proxy.
     */
    private final ProxyConfig config;

    /**
     * Name of the origin component.
     */
    private String originName = null;

    /**
     * Status of the proxied component. This will be cached after call to threshold to return to subsequent call for status (why this is called at all is a mystery to me).
     */
    ConnectorStatusResponse status = new ConnectorStatusResponse( new Status(HealthColor.NONE, Collections.emptyList()));

    public static final String API_PATH = "/api/v2/";
    public static final String THRESHOLDS_PATH = API_PATH + "component/$name/thresholds/";
    public static final String ACCUMULATORS_PATH = API_PATH + "component/$name/accumulators/";

    public static final String INFO_PATH = API_PATH + "component/$name/info/";

    public static final String CONFIG_PATH = API_PATH + "component/$name/config/";

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(ProxyComponentConnector.class);


    public ProxyComponentConnector(ProxyConfig proxyConfig, String originName) {
        this.config = proxyConfig;
        this.originName = originName;

    }

    @Override
    public void configure(ComponentConfig connectorConfig) {
        throw new UnsupportedOperationException("This connector is not supposed to be used for configuration");
    }

    @Override
    public ConnectorStatusResponse getNewStatus() {
        return status;
    }

    @Override
    public ConnectorThresholdsResponse getThresholds() {
        try {
            HashMap<String, Object> apiResult = getApiResult(THRESHOLDS_PATH);
            LinkedTreeMap<String, Object> results = (LinkedTreeMap<String, Object>)apiResult.get("results");
            List<LinkedTreeMap<String, Object>> thresholds = (List<LinkedTreeMap<String, Object>>) results.get("thresholds");
            List<ThresholdDataItem> items = new LinkedList<>();

            //reset status.
            HealthColor current = HealthColor.NONE;

            for (LinkedTreeMap<String, Object> threshold : thresholds) {
                ThresholdDataItem item = new ThresholdDataItem();
                item.setName((String) threshold.get("name"));
                item.setStatus(HealthColor.forName((String)threshold.get("status")));
                item.setLastValue((String) threshold.get("lastValue"));
                item.setStatusChangeTimestamp(System.currentTimeMillis());
                items.add(item);

                if (item.getStatus().isWorse(current))
                    current = item.getStatus();
            }

            return new ConnectorThresholdsResponse(items);
        }catch(IOException e){
            log.error("can't retrieve thresholds from "+config, e);
            return new ConnectorThresholdsResponse();
        }
    }

    @Override
    public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames)  {
        System.out.println("Accumulators requested for "+accumulatorNames);
        return null;
    }

    @Override
    public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {
        HashMap<String, Object> apiResult = getApiResult(ACCUMULATORS_PATH);
        if (apiResult != null) {
            LinkedTreeMap<String, Object> results = (LinkedTreeMap<String, Object>)apiResult.get("results");
            List<String> names = (List<String>) results.get("accumulators");
            return new ConnectorAccumulatorsNamesResponse(names);
        }

        return new ConnectorAccumulatorsNamesResponse(Collections.emptyList());
    }

    @Override
    public ConnectorConfigResponse getConfig(){
        String configString = null;
        try {
            HashMap<String, Object> apiResult = getApiResult(CONFIG_PATH);
            LinkedTreeMap<String, Object> results = (LinkedTreeMap<String, Object>)apiResult.get("results");
            configString = results.get("config").toString();
        }catch(IOException e){
            configString = "ERROR: "+e.getMessage();
            log.error("can't retrieve config from "+config, e);
        }

        ConnectorConfigResponse ret = new ConnectorConfigResponse();
        ret.setConfig(configString);
        return ret;

    }




    @Override
    public ConnectorInformationResponse getInfo() {
        System.out.println("info names requested for");
        ConnectorInformationResponse response = new ConnectorInformationResponse();
        Map<String,String> info = new HashMap<>();
        response.setInfo(info);

        //Add some useful information:
        //info.put("proxyConfig", config.toString());
        //info.put("proxyOriginName", originName);


        try {
            HashMap<String, Object> apiResult = getApiResult(INFO_PATH);
            LinkedTreeMap<String, Object> results = (LinkedTreeMap<String, Object>)apiResult.get("results");
            for (Map.Entry<String, Object> entry : results.entrySet()) {
                info.put(entry.getKey(), entry.getValue().toString());
            }
            response.setInfo(info);
            System.out.println("RETURNING INFO: "+info);
            System.out.println("RESP: "+response);
            return response;
        }catch(IOException e){
            e.printStackTrace();
            return response;
        }
    }


    private HashMap<String, Object> getApiResult(String path) throws IOException {
        System.out.println(this + " getApiResult("+path+")");
        String url  = config.getUrl();
        if (url.endsWith("/"))
            url = url.substring(0, url.length()-1);
        url += path;
        url = url.replace("$name", originName);
        System.out.println("URL: "+url);
        //this method is temporarly, we will make it smarter later.
        //for now, just call the update method of the proxy.
        String content = null;
        try {
            content = HttpHelper.getURLContent(url);
        } catch (Exception any) {
            any.printStackTrace();
        }
        System.out.println("Retrieved content: " + content);
        if (content == null)
            return null;


        Gson gson = new Gson();
        HashMap<String, Object> map = gson.fromJson(content, HashMap.class);

        Boolean success = (Boolean) map.get("success");
        if (!success) {
            System.out.println("No success....");
            return null;
        }
        return map;

    }

    @Override
    public boolean supportsThresholds() {
        return true;
    }

    @Override
    public boolean supportsAccumulators() {
        return true;
    }
}

