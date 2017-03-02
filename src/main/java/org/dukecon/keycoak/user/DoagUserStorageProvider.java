package org.dukecon.keycoak.user;

import org.dukecon.keycoak.user.doag.DoagService;
import org.dukecon.keycoak.user.doag.DoagUser;
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

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
public class DoagUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final DoagService doagService;

    public DoagUserStorageProvider(KeycloakSession session, ComponentModel model, DoagService doagService) {
        this.session = session;
        this.model = model;
        this.doagService = doagService;
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        String externalId = StorageId.externalId(id);
        DoagUser doagUser = doagService.getUserById(externalId);
        if (null != doagUser) {
            return new UserAdapter(session, realm, model, doagUser);
        } else {
            return null;
        }
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        DoagUser doagUser = doagService.getUser(username);
        if (null != doagUser) {
            return new UserAdapter(session, realm, model, doagUser);
        } else {
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
        return doagService.validateUserPassword(realUsername, cred.getValue());
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
}
