package org.moskito.control.connectors.httputils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.moskito.control.config.HttpMethodType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private static CloseableHttpClient httpClient = null;
    //private static HttpClientContext httpClientContext = null;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(3, TimeUnit.SECONDS);
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Charset.forName("UTF-8")).build();

        connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20000).setSocketTimeout(20000).setConnectionRequestTimeout(30000).build();
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager).disableCookieManagement().build();

        IdleConnectionMonitorThread connectionMonitor = new IdleConnectionMonitorThread(connectionManager);
        connectionMonitor.start();
    }

    /**
     * Executes a request using the given URL. If response has status code of 200(SC_OK)
     * returns content of its HttpEntity, otherwise - {@code null}.
     *
     * @param url the http URL to connect to.
     * @return content of HttpEntity from response.
     * @throws IOException             in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     * @see #isScOk(HttpResponse)
     */
    public static String getURLContent(String url) throws IOException {
        return getResponseContent(getHttpResponse(url));

    }

    /**
     * Executes a request using the given URL.
     *
     * @param url the http URL to connect to.
     * @return the response to the request.
     * @throws IOException             in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    public static CloseableHttpResponse getHttpResponse(String url) throws IOException {
        return getHttpResponse(url, null);
    }

    /**
     * Executes a request using the given URL and credentials.
     *
     * @param url         the http URL to connect to.
     * @param credentials credentials to use
     * @return the response to the request.
     * @throws IOException             in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    public static CloseableHttpResponse getHttpResponse(String url, UsernamePasswordCredentials credentials) throws IOException {
        return getHttpResponse(url, credentials, null);
    }

    /**
     * Executes a request using the given URL and credentials.
     *
     * @param url         the http URL to connect to.
     * @param credentials credentials to use
     * @param header      custom header
     * @return the response to the request.
     * @throws IOException             in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    public static CloseableHttpResponse getHttpResponse(String url, UsernamePasswordCredentials credentials, Header header) throws IOException {
        HttpGet request = new HttpGet(url);
        HttpClientContext httpClientContext = HttpClientContext.create();

        if (credentials != null) {
            httpClientContext.setCredentialsProvider(new BasicCredentialsProvider());
            URI uri = request.getURI();
            AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
            httpClientContext.getCredentialsProvider().setCredentials(authScope, credentials);
        }
        if (header != null) {
            request.setHeader(header);
        }
        return httpClient.execute(request, httpClientContext);
    }

    /**
     * Executes a request using the given URL credentials, method, payload, content type and headers.
     *
     * @param url         the http URL to connect to.
     * @param credentials credentials to use
     * @param headers     custom headers
     * @param methodType  request method
     * @param payload     request payload
     * @param contentType request content type
     * @return the response to the request.
     * @throws IOException             in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    public static CloseableHttpResponse getHttpResponse(String url, UsernamePasswordCredentials credentials, Header[] headers, HttpMethodType methodType, String payload, ContentType contentType) throws IOException {
        validate(methodType, contentType);

        HttpRequestBase request;
        if (methodType != null) {
            switch (methodType) {
                case GET:
                    request = new HttpGet(url);
                    break;
                case POST:
                    request = new HttpPost(url);
                    setPayload((HttpEntityEnclosingRequestBase) request, payload, contentType);
                    break;
                case PATCH:
                    request = new HttpPatch(url);
                    setPayload((HttpEntityEnclosingRequestBase) request, payload, contentType);
                    break;
                case PUT:
                    request = new HttpPut(url);
                    setPayload((HttpEntityEnclosingRequestBase) request, payload, contentType);
                    break;
                case DELETE:
                    request = new HttpDelete(url);
                    break;
                default:
                    throw new IllegalArgumentException("Cannot handle method type of" + methodType);
            }
        } else {
            request = new HttpGet(url);
        }

        HttpClientContext httpClientContext = HttpClientContext.create();

        if (credentials != null) {
            httpClientContext.setCredentialsProvider(new BasicCredentialsProvider());
            URI uri = request.getURI();
            AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
            httpClientContext.getCredentialsProvider().setCredentials(authScope, credentials);
        }

        if(headers != null) {
            request.setHeaders(headers);
        }

        return httpClient.execute(request, httpClientContext);
    }

    public static void setPayload(HttpEntityEnclosingRequestBase request, String payload, ContentType contentType) {
        StringEntity json = new StringEntity(payload, contentType);
        request.setEntity(json);
    }

    public static void validate(HttpMethodType methodType, ContentType contentType) throws IOException {
        if (methodType != null && !methodType.equals(HttpMethodType.GET)) {
            if (contentType == null) {
                throw new IOException("Content type for non-GET method cannot be null");
            }
            List<String> textMimes = new LinkedList<>(Arrays.asList("application/json", "application/xml", "text/plain", "text/xml", "application/xhtml+xml", "text/html"));
            if (!textMimes.contains(contentType.getMimeType())) {
                throw new IOException("Cannot handle content type of mime. Only text is allowed");
            }
        }
    }

    /**
     * Compare two instances of Credentials.
     *
     * @param c1 instance of Credentials
     * @param c2 another instance of Credentials
     * @return comparison result. {@code true} if both are null or contain same user/password pairs, false otherwise.
     */
    private static boolean areSame(Credentials c1, Credentials c2) {
        if (c1 == null) {
            return c2 == null;
        } else {
            return StringUtils.equals(c1.getUserPrincipal().getName(), c1.getUserPrincipal().getName()) &&
                    StringUtils.equals(c1.getPassword(), c1.getPassword());
        }
    }

    /**
     * Check if HttpResponse contains status code 200(SC_OK).
     *
     * @param response instance of HttpResponse.
     * @return {@code true} if HttpResponse contains status code 200(SC_OK), {@code false} otherwise.
     */
    public static boolean isScOk(HttpResponse response) {
        return response != null && response.getStatusLine() != null &&
                response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }

    /**
     * Get text content from the response if response status code is 200.
     *
     * @param response instance of HttpResponse.
     * @return String representation of HttpEntity.
     * @throws IOException if an I/O error occurs
     * @see #isScOk(HttpResponse)
     */
    public static String getResponseContent(CloseableHttpResponse response) throws IOException {
        final HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                entity.writeTo(out);//call this in any case!!!
                return isScOk(response) ? new String(out.toByteArray(), Charset.forName("UTF-8")) : null;
            } finally {
                //ensure entity is closed.
                try {
                    EntityUtils.consume(entity);
                } finally {
                    response.close();
                }

            }
        } else {
            return null;
        }
    }


}
