package org.dukecon.keycoak.user;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
public class DoagService {

    private static final String ART_PWD = "1";
    private static final String ART_USER = "3";

    private DoagClient doagClient;

    public DoagService(String url, String key) {
        buildClient(url, key);
    }

    private void buildClient(String url, String key) {
        ResteasyClient client = new ResteasyClientBuilder()
                .register(new AuthFilter(key))
                .connectionPoolSize(10)
                .maxPooledPerRoute(5)
                .disableTrustManager()
                .build();
        ResteasyWebTarget target = client.target(url);
        this.doagClient = target.proxyBuilder(DoagClient.class).classloader(this.getClass().getClassLoader()).build();
    }

    public DoagUser getUser(String username) {
        DoagUser user = null;
        String userString = doagClient.getUser(ART_USER, username);
        System.out.println(userString);
        if (!userString.startsWith("false")) {
            String[] userData = userString.split(";");
            String id = userData[0].trim();
            String lastname = userData[1].trim();
            String firstname = userData[2].trim();
            user = new DoagUser(id, firstname, lastname, username);
        }
        return user;
    }

    public boolean validateUserPassword(String username, String password) {
        String validationResult = doagClient.validatePassword(ART_PWD, username, password);
        return Boolean.parseBoolean(validationResult.trim());
    }

    public class AuthFilter implements ClientRequestFilter {
        private String key;

        AuthFilter(String key) {
            this.key = key;
        }

        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            MultivaluedMap<String, Object> headers = requestContext.getHeaders();
            headers.add("key", key);
        }
    }
}
