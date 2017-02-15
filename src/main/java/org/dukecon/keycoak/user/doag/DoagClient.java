package org.dukecon.keycoak.user.doag;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
public interface DoagClient {
    @GET
    String getUser(@HeaderParam("art") String art,
                   @HeaderParam("user") String username);

    @GET
    String validatePassword(@HeaderParam("art") String art,
                            @HeaderParam("user") String username,
                            @HeaderParam("password") String password);
}
