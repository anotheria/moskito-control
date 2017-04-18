package org.moskito.control.connectors.httputils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
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

		httpClient = new DefaultHttpClient(cm);
	}

	/**
	 * Executes a request using the given URL. If response has status code of 200(SC_OK)
	 * returns content of its HttpEntity, otherwise - {@code null}.
	 * @param url the http URL to connect to.
	 *
	 * @return content of HttpEntity from response.
	 * @throws IOException in case of a problem or the connection was aborted
	 * @throws ClientProtocolException in case of an http protocol error
	 * @see #isScOk(HttpResponse)
	 */
	public static String getURLContent(String url) throws IOException {
		return getResponseContent(getHttpResponse(url));

	}

	/**
	 * Executes a request using the given URL.
	 *
	 * @param url  the http URL to connect to.
	 *
	 * @return  the response to the request.
	 * @throws IOException in case of a problem or the connection was aborted
	 * @throws ClientProtocolException in case of an http protocol error
	 */
	public static HttpResponse getHttpResponse(String url) throws IOException {
		return httpClient.execute(new HttpGet(url));
	}

	/**
	 * Check if HttpResponse contains status code 200(SC_OK).
	 * @param response instance of HttpResponse.
	 * @return {@code true} if HttpResponse contains status code 200(SC_OK), {@code false} otherwise.
	 */
	public static boolean isScOk(HttpResponse response) {
		return response != null && response.getStatusLine()!= null &&
				response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
	}

	/**
	 * Get text content from the response if response status code is 200.
	 * @param response instance of HttpResponse.
	 * @return String representation of HttpEntity.
	 * @throws IOException if an I/O error occurs
	 * @see #isScOk(HttpResponse)
	 */
	public static String getResponseContent(HttpResponse response) throws IOException {
		final HttpEntity entity = response.getEntity();
		if (entity != null) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				entity.writeTo(out);//call this in any case!!!
				return isScOk(response) ? new String(out.toByteArray(), Charset.forName("UTF-8")) : null;
			} finally {
				//ensure entity is closed.
				EntityUtils.consume(entity);
			}
		} else {
			return null;
		}
	}


}
