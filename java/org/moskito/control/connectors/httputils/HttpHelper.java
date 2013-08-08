package org.moskito.control.connectors.httputils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * This help class is a wrapper around apache http client lib.
 *
 * @author lrosenberg
 * @since 15.07.13 11:14
 */
public class HttpHelper {

	/**
	 * HttpClient instance.
	 */
	private static HttpClient httpClient = null;//new DefaultHttpClient();

	static{
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
				new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(
				new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		// Increase max connections for localhost:80 to 50

		httpClient = new DefaultHttpClient(cm);
	}


	public static String getURLContent(String url) throws IOException {
		//System.out.println("%%% Getting "+url);
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpget);
		//System.out.println("Response: "+response);
		HttpEntity entity = response.getEntity();
		if (response.getStatusLine().getStatusCode()!=200){
			//System.out.println("CALL RETURNED null");
			return null;
		}
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			entity.writeTo(out);
			//ensure the stream is closed.
			return new String(out.toByteArray(), Charset.forName("UTF-8"));
		}finally{
			try{
				//ensure entity is closed.
				EntityUtils.consume(entity);
			}catch(Exception ignored){}
		}

	}


}
