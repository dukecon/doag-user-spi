package org.dukecon.keycoak.user;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
public class DoagUserStorageProviderFactory implements UserStorageProviderFactory<DoagUserStorageProvider> {

    @Override
    public DoagUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        // here you can setup the user storage provider, initiate some connections, etc.

        DoagRepository repository = new DoagRepository();

        return new DoagUserStorageProvider(session, model, repository);
    }

    @Override
    public String getId() {
        return "doag-user-provider";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return ProviderConfigurationBuilder.create()
                .property("url", "URL", "", ProviderConfigProperty.STRING_TYPE, "", null)
                .property("key", "Key", "", ProviderConfigProperty.STRING_TYPE, "", null)
                .build();
    }
}
