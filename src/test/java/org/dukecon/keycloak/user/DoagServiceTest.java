package org.dukecon.keycloak.user;

import org.dukecon.keycoak.user.doag.DoagService;
import org.dukecon.keycoak.user.doag.DoagUser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
public class DoagServiceTest {

    private String url = System.getProperty("doag.url");
    private String key = System.getProperty("doag.key");
    private String userId = System.getProperty("doag.userId");
    private String username = System.getProperty("doag.user");
    private String password = System.getProperty("doag.password");

    DoagService doagService = new DoagService(url, key);

    @Test
    public void testGetUser() {
        DoagUser user = doagService.getUser(username);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testGetUser_fail() {
        DoagUser user = doagService.getUser("johndoe");
        assertNull(user);
    }

    @Test
    public void testValidateUser() {
        boolean result = doagService.validateUserPassword(username, password);
        assertTrue(result);
    }

    @Test
    public void testValidateUser_fail() {
        boolean result = doagService.validateUserPassword(username, "");
        assertFalse(result);
    }

    @Test
    public void testGetUserById() {
        DoagUser user = doagService.getUserById(userId);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testGetUserById_fail() {
        DoagUser user = doagService.getUserById("4711");
        assertNull(user);
    }

}
