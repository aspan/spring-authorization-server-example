CREATE TABLE oauth2_client_registration
(
    client_registration_id                               varchar(100) NOT NULL,
    client_authentication_method                         varchar(100) DEFAULT NULL,
    client_authorization_grant_type                      varchar(100) DEFAULT NULL,
    client_id                                            varchar(100) DEFAULT NULL,
    client_name                                          varchar(100) DEFAULT NULL,
    client_redirect_uri                                  varchar(255) DEFAULT NULL,
    client_scopes                                        text,
    client_secret                                        varchar(100) DEFAULT NULL,
    provider_authorization_uri                           varchar(255) DEFAULT NULL,
    provider_configuration_metadata                      text,
    provider_issuer_uri                                  varchar(255) DEFAULT NULL,
    provider_jwk_set_uri                                 varchar(255) DEFAULT NULL,
    provider_token_uri                                   varchar(255) DEFAULT NULL,
    provider_user_info_endpoint_authentication_method    varchar(100) DEFAULT NULL,
    provider_user_info_endpoint_uri                      varchar(255) DEFAULT NULL,
    provider_user_info_endpoint_user_name_attribute_name varchar(100) DEFAULT NULL,
    PRIMARY KEY (client_registration_id)
);