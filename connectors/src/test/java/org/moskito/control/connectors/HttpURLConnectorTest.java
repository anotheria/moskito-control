package org.moskito.control.connectors;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;
import net.anotheria.util.StringUtils;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.HttpMethodType;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.common.HealthColor;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Basic test for {@link HttpURLConnector} class.
 *
 * @author dzhmud
 * @since 17.04.2017 4:40 PM
 */
public class HttpURLConnectorTest {
    private static HttpClient httpClient = null;
    private static boolean readyForTest = false;

    @BeforeClass
    public static void beforeClass() {
        HttpClient testClient = new HttpClientWrapper(MockFactory.createMock(HttpClient.class, new TestHttpClient()));
        try {
            Field field = HttpHelper.class.getDeclaredField("httpClient");
            field.setAccessible(true);
            httpClient = HttpClient.class.cast(field.get(null));
            field.set(null, testClient);
            readyForTest = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void afterClass() {
        try {
            Field field = HttpHelper.class.getDeclaredField("httpClient");
            field.setAccessible(true);
            field.set(null, httpClient);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private ComponentConfig makeComponentConfig(String location){
        ComponentConfig ret = new ComponentConfig();
        ret.setName("testName");
        ret.setLocation(location);
        return ret;
    }

    @Test
    public void test() {
        if (!readyForTest) return;

        Connector urlConnector = new HttpURLConnector();

        urlConnector.configure(makeComponentConfig(null));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.PURPLE);

        urlConnector.configure(makeComponentConfig(""));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.PURPLE);

        urlConnector.configure(makeComponentConfig("YELLOW"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.YELLOW);
        urlConnector.configure(makeComponentConfig("yellow"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.YELLOW);

        urlConnector.configure(makeComponentConfig("RED"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.RED);

        urlConnector.configure(makeComponentConfig("FAILED"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.RED);

        urlConnector.configure(makeComponentConfig("DOWN"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.RED);

        urlConnector.configure(makeComponentConfig("greeeeen"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.GREEN);

        urlConnector.configure(makeComponentConfig("test?code=200"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.GREEN);

        urlConnector.configure(makeComponentConfig("test?code=200&status=some_html_entity_here"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.GREEN);

        urlConnector.configure(makeComponentConfig("test?code=404&status=FileNotFound"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.RED);

        urlConnector.configure(makeComponentConfig("test?throw=IOException"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.PURPLE);

        urlConnector.configure(makeComponentConfig("test?throw=anyRuntimeException"));
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.PURPLE);

    }

    /**
     * Mock for HttpClient that creates response based on request URI.
     */
    public static class TestHttpClient implements Mocking {
        /**
         * null -> null
         * yellow,red,fail,etc -> 200.SC_OK + text as entity
         * [test]?code=503&status=ServiceUnavailable -> status code + status
         * [test]?throw=java.io.IOException -> throw exception
         */
        @SuppressWarnings("unused")
        public HttpResponse execute(HttpUriRequest request) throws IOException {
            String uri = request.getURI().toString();
            String exception = null, status = null, code = null;

            if (request.getURI().getQuery()!= null) {
                Map<String,String> params = StringUtils.buildParameterMap(
                        request.getURI().getQuery(), '&', '=');
                exception = params.get("throw");
                status = params.get("status");
                code = params.get("code");
            }

            final HttpResponse response;
            if (StringUtils.isEmpty(uri) || "null".equalsIgnoreCase(uri)) {
                response = null;
            } else if (exception != null) {
                if (exception.endsWith("IOException")) {
                    throw new IOException("test exception"){
                        @Override public StackTraceElement[] getStackTrace() {return null;}
                    };
                } else {
                    throw new RuntimeException("test exception"){
                        @Override public StackTraceElement[] getStackTrace() {return null;}
                    };
                }
            } else {
                if (code == null) {
                    response = MockFactory.createMock(HttpResponse.class,
                            new ResponseMocking(200,"SC_OK", uri));
                } else {
                    response = MockFactory.createMock(HttpResponse.class,
                            new ResponseMocking(Integer.parseInt(code), status, uri));
                }
            }
            return response;
        }

    }

    /**
     * Mock for HttpResponse class.
     */
    public static class ResponseMocking implements Mocking {
        private final int code;
        private final String status;
        private final String entity;

        ResponseMocking(int code, String status, String entity) {
            this.code = code;
            this.status = status;
            this.entity = entity;
        }
        @SuppressWarnings("unused")
        public StatusLine getStatusLine() {
            return new StatusLine() {
                @Override public ProtocolVersion getProtocolVersion() {
                    return null;
                }
                @Override public int getStatusCode() {
                    return code;
                }
                @Override public String getReasonPhrase() {
                    return status;
                }
            };
        }
        @SuppressWarnings("unused")
        public HttpEntity getEntity() {
            return new BasicHttpEntity(){
                public void writeTo(OutputStream outstream) throws IOException {
                    outstream.write(entity.getBytes(Charset.forName("UTF-8")));
                    outstream.flush();
                    outstream.close();
                }
            };
        }

    }

    /**
     * Wrapper class used to replace HttpHelper.httpClient object in test.
     * Needs to be an instance of AbstractHttpClient, not the HttpClient impl.
     */
    public static class HttpClientWrapper extends AbstractHttpClient {
        private final HttpClient client;
        private HttpClientWrapper(HttpClient client) {
            super(null,null);
            this.client = client;
        }

        @Override protected HttpParams createHttpParams() {
            return new BasicHttpParams();
        }

        @Override protected BasicHttpProcessor createHttpProcessor() {
            return new BasicHttpProcessor();
        }
        @Override protected RequestDirector createClientRequestDirector(
                final HttpRequestExecutor requestExec,
                final ClientConnectionManager conman,
                final ConnectionReuseStrategy reustrat,
                final ConnectionKeepAliveStrategy kastrat,
                final HttpRoutePlanner rouplan,
                final HttpProcessor httpProcessor,
                final HttpRequestRetryHandler retryHandler,
                final RedirectStrategy redirectStrategy,
                final AuthenticationStrategy targetAuthStrategy,
                final AuthenticationStrategy proxyAuthStrategy,
                final UserTokenHandler userTokenHandler,
                final HttpParams params) {
            return new RequestDirector() {
                @Override public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException, IOException {
                    return client.execute(HttpUriRequest.class.cast(request));
                }
            };
        }
    }

}
