package org.dukecon.keycloak.user;

import org.dukecon.keycoak.user.doag.DoagService;
import org.jboss.logging.Logger;

/**
 * @author Gerd Aschemann (gerd@aschemann.net)
 */
public class DoagValidatePassword {

    private static final Logger logger = Logger.getLogger(DoagValidatePassword.class);

    /**
     * Check password for given user.
     *
     * @param args
     * #0: DOAG Service URL
     * #1: DOAG Service Key
     * #2: Username
     * #3: Password
     */
    public static void main(String[] args) {
        DoagService doagService = new DoagService(args[0], args[1]);
        boolean valid = doagService.validateUserPassword(args[2], args[3]);
        logger.infov("Password is {0}", (valid ? "valid" : "invalid"));
    }
}
