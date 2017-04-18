package org.moskito.control.connectors;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;
import net.anotheria.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.core.HealthColor;

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
        HttpClient testClient = MockFactory.createMock(HttpClient.class, new TestHttpClient());
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

    @Test
    public void test() {
        if (!readyForTest) return;

        Connector urlConnector = new HttpURLConnector();

        urlConnector.configure(null, null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.PURPLE);

        urlConnector.configure("", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.PURPLE);

        urlConnector.configure("YELLOW", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.YELLOW);
        urlConnector.configure("yellow", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.YELLOW);

        urlConnector.configure("RED", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.RED);

        urlConnector.configure("FAILED", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.RED);

        urlConnector.configure("DOWN", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.RED);

        urlConnector.configure("greeeeen", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.GREEN);

        urlConnector.configure("test?code=200", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.GREEN);

        urlConnector.configure("test?code=200&status=some_html_entity_here", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.GREEN);

        urlConnector.configure("test?code=404&status=FileNotFound", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.RED);

        urlConnector.configure("test?throw=IOException", null);
        assertTrue(urlConnector.getNewStatus().getStatus().getHealth() == HealthColor.PURPLE);

        urlConnector.configure("test?throw=anyRuntimeException", null);
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
                    throw new IOException("test exception");
                } else {
                    throw new RuntimeException("test exception");
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
}
