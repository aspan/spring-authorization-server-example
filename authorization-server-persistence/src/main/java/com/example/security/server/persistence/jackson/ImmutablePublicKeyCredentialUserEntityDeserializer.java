package com.example.security.server.persistence.jackson;

import java.util.Base64;

import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;


public class ImmutablePublicKeyCredentialUserEntityDeserializer extends ValueDeserializer<ImmutablePublicKeyCredentialUserEntity> {
    @Override
    public ImmutablePublicKeyCredentialUserEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        var jsonNode = deserializationContext.readTree(jsonParser);
        return (ImmutablePublicKeyCredentialUserEntity) ImmutablePublicKeyCredentialUserEntity.builder()
                                                                                              .displayName(jsonNode.get("displayName").asString())
                                                                                              .id(new Bytes(Base64.getDecoder().decode(jsonNode.get("id").get("bytes").asString().getBytes())))
                                                                                              .name(jsonNode.get("name").asString())
                                                                                              .build();
    }
}
