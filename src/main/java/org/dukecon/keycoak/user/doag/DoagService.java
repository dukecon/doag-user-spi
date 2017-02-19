package org.dukecon.keycoak.user.doag;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
public class DoagService {

    private static final Logger logger = Logger.getLogger(DoagService.class);

    private static final String ART_PWD = "1";
    private static final String ART_USER = "3";

    private DoagClient doagClient;

    public DoagService(String url, String key) {
        buildClient(url, key);
    }

    private void buildClient(String url, String key) {
        ResteasyClient client = new ResteasyClientBuilder()
                .register(new AuthFilter(key))
                .register(new RestLoggingFilter())
                .connectionPoolSize(10)
                .maxPooledPerRoute(5)
                .disableTrustManager()
                .build();
        ResteasyWebTarget target = client.target(url);
        this.doagClient = target.proxyBuilder(DoagClient.class).classloader(this.getClass().getClassLoader()).build();
    }

    public DoagUser getUser(String username) {
        DoagUser user = null;
        try {
            String userString = doagClient.getUser(ART_USER, username);
            if (!userString.startsWith("false")) {
                String[] userData = userString.split(";");
                String id = userData[0].trim();
                String lastname = userData[1].trim();
                String firstname = userData[2].trim();
                user = new DoagUser(id, firstname, lastname, username);
            } else {
                logger.warn("Got false response requesting user data for user " + username);
            }
        } catch (Exception e) {
            handleException(e, "getUser");
        }
        return user;
    }

    public boolean validateUserPassword(String username, String password) {
        try {
            String validationResult = doagClient.validatePassword(ART_PWD, username, password);
            return Boolean.parseBoolean(validationResult.trim());
        } catch (Exception e) {
            handleException(e, "validatePassword");
            return false;
        }
    }

    private void handleException(Exception ex, String serviceName) {
        if (ex instanceof ClientErrorException) {
            ClientErrorException cee = (ClientErrorException) ex;
            Response response = cee.getResponse();
            Response.StatusType statusInfo = response.getStatusInfo();
            logger.error("Error while calling REST-Serivce " + serviceName + ", got response: " + statusInfo.getStatusCode() + " " + statusInfo.getReasonPhrase());
        } else {
            logger.error("Exception while calling REST-Service " + serviceName, ex);
        }
    }
}
