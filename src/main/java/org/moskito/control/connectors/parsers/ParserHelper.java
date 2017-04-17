package org.moskito.control.connectors.parsers;

import net.anotheria.util.StringUtils;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.util.Map;

/**
 * Helper class containing small but common parsing tasks.
 *
 * @author dzhmud
 * @since 14.04.2017 8:09 PM
 */
public class ParserHelper {

    private static final String LOGIN_PARAM_NAME = "login";
    private static final String PASSWORD_PARAM_NAME = "password";

    private ParserHelper(){}

    /**
     * Attempts to parse given String value and get login and password values from it.
     * Given parameter should look like "login=loginValue&password=passValue".
     * @param credentials serialized form of login/password pair.
     * @return {@link UsernamePasswordCredentials} object containing login and password(optional). Will
     * return null if parameter given is empty or does not contain 'login' parameter or uses different separators.
     */
    public static UsernamePasswordCredentials getCredentials(String credentials) {
        UsernamePasswordCredentials result = null;
        if (!StringUtils.isEmpty(credentials)) {
            Map<String, String> params = StringUtils.buildParameterMap(credentials, '&', '=');
            final String username = params.get(LOGIN_PARAM_NAME);
            final String password = params.get(PASSWORD_PARAM_NAME);
            if (!StringUtils.isEmpty(username)) {
                result = new UsernamePasswordCredentials(username, password);
            }
        }
        return result;
    }

}
