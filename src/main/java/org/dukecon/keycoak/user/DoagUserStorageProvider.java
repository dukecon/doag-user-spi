package org.dukecon.keycoak.user;

import org.dukecon.keycoak.user.doag.DoagService;
import org.dukecon.keycoak.user.doag.DoagUser;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
public class DoagUserStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, CredentialInputValidator {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final DoagService doagService;

    private static final Logger logger = Logger.getLogger(DoagUserStorageProvider.class);

    public DoagUserStorageProvider(KeycloakSession session, ComponentModel model, DoagService doagService) {
        this.session = session;
        this.model = model;
        this.doagService = doagService;
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        logger.info("Getting user for id: " + id);

        String externalId = StorageId.externalId(id);
        DoagUser doagUser = doagService.getUserById(externalId);
        if (null != doagUser) {
            logger.info("Getting user for id: " + id + " successful (DoagUserId: " + doagUser.getId() + ")");

            UserCache.addUser(doagUser);
            return new UserAdapter(session, realm, model, doagUser);
        } else {
            logger.info("Getting user for id: " + id + " failed");


            return null;
        }
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        logger.info("Getting user for username: " + username);

        DoagUser doagUser = doagService.getUser(username);

        if (null != doagUser) {
            logger.info("Getting user for username: " + username + " successful (DoagUserId: " + doagUser.getId() + ")");

            UserCache.addUser(doagUser);
            return new UserAdapter(session, realm, model, doagUser);
        } else {
            logger.info("Getting user for username: " + username + " failed");

            return null;
        }
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return getUserByUsername(email, realm);
    }


    @Override
    public boolean supportsCredentialType(String credentialType) {
        return CredentialModel.PASSWORD.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
            return false;
        }
        UserCredentialModel cred = (UserCredentialModel) input;
        String realUsername = user.getFirstAttribute(UserAdapter.REAL_USERNAME_ATTRIBUTE);
        return doagService.validateUserPassword(realUsername, cred.getChallengeResponse());
    }

    @Override
    public void preRemove(RealmModel realm) {
    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {
    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {
    }

    @Override
    public void close() {
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        return UserCache.getCount();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        return UserCache.getUsers().stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        return getUsers(realm);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        return UserCache.findUsers(search).stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        return searchForUser(search, realm);
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        return null;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        return Collections.emptyList();
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        return Collections.emptyList();
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        return null;
    }
}
