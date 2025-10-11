package com.example.security.persistence.jackson;

import org.springframework.security.web.webauthn.api.Bytes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = ImmutablePublicKeyCredentialUserEntityDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImmutablePublicKeyCredentialUserEntityMixIn {
    public ImmutablePublicKeyCredentialUserEntityMixIn(@JsonProperty("name") String name,
                                                       @JsonProperty("id") Bytes id,
                                                       @JsonProperty("displayName") String displayName) {
    }
}
