package org.dukecon.keycoak.user.doag;

import org.jboss.logging.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Provider
public class RestLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final Logger logger = Logger.getLogger(RestLoggingFilter.class);

    @Override
    public void filter(ClientRequestContext clientRequestContext) {
        String username = (String) clientRequestContext.getHeaders().getFirst("user");
        logger.infov("***** REST REQUEST TO URL ''{0}'' FOR USER ''{1}''", clientRequestContext.getUri(), username);
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) {
        logger.infov("***** REST RESPONSE STATUS: ''{0}''", clientResponseContext.getStatusInfo().getReasonPhrase());
    }
}
