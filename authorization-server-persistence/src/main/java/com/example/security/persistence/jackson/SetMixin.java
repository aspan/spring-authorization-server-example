package com.example.security.persistence.jackson;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class SetMixin {
    @JsonCreator
    SetMixin(Collection<?> collection) {
    }
}
