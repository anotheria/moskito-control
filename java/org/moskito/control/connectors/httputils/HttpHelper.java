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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.07.13 11:14
 */
public class HttpHelper {

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
		System.out.println("%%% Getting "+url);
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpget);
		System.out.println("Response: "+response);
		HttpEntity entity = response.getEntity();
		System.out.println("%%% ENTITY "+entity);
		System.out.println("%%% ENTITY length: " + entity.getContentLength() + ", STR: " + entity.isStreaming() + ", CH: " + entity.isChunked() + " R: " + entity.isRepeatable());
		if (response.getStatusLine().getStatusCode()!=200){
			System.out.println("CALL RETURNED null");
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		entity.writeTo(out);
		String ret = new String(out.toByteArray());
		return ret;


/*
		StringBuilder ret = new StringBuilder();
		if (entity != null) {
			InputStream in = entity.getContent();
			try {
				System.out.println("%%% "+in.available() + " available");
				while(in.available()>0){
					System.out.println("%%% "+in.available() + " available");
					byte[] data = new byte[in.available()];
					in.read(data);
					System.out.println ("%%% read "+new String(data));
					ret.append(data);
				}
			} finally {
				in.close();
			}
		}

		System.out.println("%%% returning "+ret.toString());

		return ret.toString();
*/
	}


}
