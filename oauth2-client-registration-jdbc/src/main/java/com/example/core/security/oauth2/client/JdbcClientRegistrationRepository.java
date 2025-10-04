package com.example.core.security.oauth2.client;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdbcClientRegistrationRepository implements ClientRegistrationRepository {
    private final JdbcOperations jdbcOperations;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JdbcClientRegistrationRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return this.jdbcOperations.queryForObject(
                """
                        SELECT client_registration_id,
                               client_authentication_method,
                               client_authorization_grant_type,
                               client_id,
                               client_name,
                               client_redirect_uri,
                               client_scopes,
                               client_secret,
                               provider_authorization_uri,
                               provider_configuration_metadata,
                               provider_issuer_uri,
                               provider_jwk_set_uri,
                               provider_token_uri,
                               provider_user_info_endpoint_authentication_method,
                               provider_user_info_endpoint_uri,
                               provider_user_info_endpoint_user_name_attribute_name
                        FROM oauth2_client_registration
                        WHERE client_registration_id = ?
                        """,
                (rs, rowNum) -> {
                    try {
                        var builder =
                                ClientRegistration.withRegistrationId(rs.getString("client_registration_id"))
                                                  .clientId(rs.getString("client_id"))
                                                  .clientSecret(rs.getString("client_secret"))
                                                  .clientName(rs.getString("client_name"))
                                                  .redirectUri(rs.getString("client_redirect_uri"))
                                                  .scope(convertScopes(rs.getString("client_scopes")))
                                                  .authorizationUri(rs.getString("provider_authorization_uri"))
                                                  .tokenUri(rs.getString("provider_token_uri"))
                                                  .userInfoUri(rs.getString("provider_user_info_endpoint_uri"))
                                                  .userNameAttributeName(rs.getString("provider_user_info_endpoint_user_name_attribute_name"))
                                                  .jwkSetUri(rs.getString("provider_jwk_set_uri"))
                                                  .issuerUri(rs.getString("provider_issuer_uri"))
                                                  .providerConfigurationMetadata(parseMap(rs.getString("provider_configuration_metadata")));
                        var clientAuthenticationMethod = rs.getString("client_authentication_method");
                        if (clientAuthenticationMethod != null && !clientAuthenticationMethod.isBlank()) {
                            builder.clientAuthenticationMethod(new ClientAuthenticationMethod(clientAuthenticationMethod));
                        }
                        var clientAuthorizationGrantType = rs.getString("client_authorization_grant_type");
                        if (clientAuthorizationGrantType != null && !clientAuthorizationGrantType.isBlank()) {
                            builder.authorizationGrantType(new AuthorizationGrantType(clientAuthorizationGrantType));
                        }
                        var providerUserInfoEndpointAuthenticationMethod = rs.getString("provider_user_info_endpoint_authentication_method");
                        if (providerUserInfoEndpointAuthenticationMethod != null && !providerUserInfoEndpointAuthenticationMethod.isBlank()) {
                            builder.userInfoAuthenticationMethod(new AuthenticationMethod(providerUserInfoEndpointAuthenticationMethod));
                        }

                        return builder.build();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                registrationId);
    }

    public void save(ClientRegistration clientRegistration) {
        jdbcOperations.update(
                """
                        INSERT INTO oauth2_client_registration
                            (client_registration_id,
                             client_authentication_method,
                             client_authorization_grant_type,
                             client_id,
                             client_name,
                             client_redirect_uri,
                             client_scopes,
                             client_secret,
                             provider_authorization_uri,
                             provider_configuration_metadata,
                             provider_issuer_uri,
                             provider_jwk_set_uri,
                             provider_token_uri,
                             provider_user_info_endpoint_authentication_method,
                             provider_user_info_endpoint_uri,
                             provider_user_info_endpoint_user_name_attribute_name)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                clientRegistration.getRegistrationId(),
                clientRegistration.getClientAuthenticationMethod() != null ? clientRegistration.getClientAuthenticationMethod().getValue() : null,
                clientRegistration.getAuthorizationGrantType() != null ? clientRegistration.getAuthorizationGrantType().getValue() : null,
                clientRegistration.getClientId(),
                clientRegistration.getClientName(),
                clientRegistration.getRedirectUri(),
                convertScopes(clientRegistration.getScopes()),
                clientRegistration.getClientSecret(),
                clientRegistration.getProviderDetails().getAuthorizationUri(),
                writeMap(clientRegistration.getProviderDetails().getConfigurationMetadata()),
                clientRegistration.getProviderDetails().getIssuerUri(),
                clientRegistration.getProviderDetails().getJwkSetUri(),
                clientRegistration.getProviderDetails().getTokenUri(),
                clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod() != null ? clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod().getValue() : null,
                clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri(),
                clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    private Collection<String> convertScopes(String clientScopes) {
        if (clientScopes == null || clientScopes.isBlank()) {
            return Set.of();
        }

        return Set.of(clientScopes.split(","));
    }

    private String convertScopes(Collection<String> clientScopes) {
        if (clientScopes == null || clientScopes.isEmpty()) {
            return null;
        }

        return String.join(",", clientScopes);
    }

    public Map<String, Object> parseMap(String data) {
        if (data == null || data.isBlank()) {
            return Map.of();
        }
        try {
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    public String writeMap(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

}
