package com.example.auth.login.webauthn;

import java.io.IOException;
import java.util.Base64;

import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImmutablePublicKeyCredentialUserEntityDeserializer extends JsonDeserializer<ImmutablePublicKeyCredentialUserEntity> {
    @Override
    public ImmutablePublicKeyCredentialUserEntity deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode root = mapper.readTree(parser);

        return (ImmutablePublicKeyCredentialUserEntity) ImmutablePublicKeyCredentialUserEntity.builder()
                                                                                              .displayName(root.get("displayName").asText())
                                                                                              .id(new Bytes(Base64.getDecoder().decode(root.get("id").get("bytes").asText().getBytes())))
                                                                                              .name(root.get("name").asText())
                                                                                              .build();
    }
}
