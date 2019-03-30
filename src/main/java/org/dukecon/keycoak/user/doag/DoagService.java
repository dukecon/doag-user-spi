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
    private static final String ART_ID = "4";

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
            user = parseUserString(username, userString, ART_USER);
        } catch (Exception e) {
            handleException(e, "getUser");
        }
        logger.info("User: " + (user != null ? user.toString() : null));
        return user;
    }

    public DoagUser getUserById(String userId) {
        DoagUser user = null;
        try {
            String userString = doagClient.getUserById(ART_ID, userId);
            user = parseUserString(userId, userString, ART_ID);
        } catch (Exception e) {
            handleException(e, "getUserById");
        }
        logger.info("User: " + (user != null ? user.toString() : null));
        return user;
    }

    private DoagUser parseUserString(String userId, String userString, String art) {
        logger.info("Got userdata response for userId " + userId + ": " + userString.trim());
        DoagUser user = null;
        if (!userString.startsWith("false")) {
            String[] userData = userString.split(";");
            String id = userData[0].trim();
            String lastname = "", firstname = "", username = "", realUsername = "";
            if (ART_USER.equals(art)) { // getUserByUsername
                username = userId;
                lastname = userData[1].trim();
                firstname = userData[2].trim();
                realUsername = userData[3].trim();
            } else if (ART_ID.equals(art)) { // getUserById
                username = userData[1].trim();
                realUsername = userData[1].trim();
                lastname = userData[2].trim();
                firstname = userData[3].trim();
            }
            user = new DoagUser(id, username, firstname, lastname, realUsername);
        }
        return user;
    }

    public boolean validateUserPassword(String username, String password) {
        try {
            String validationResult = doagClient.validatePassword(ART_PWD, username, password);
            boolean result = Boolean.parseBoolean(validationResult.trim());
            logger.infov ("Password for ''{0}'' is {1}", username, result ? "valid" : "invalid");
            return result;
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
