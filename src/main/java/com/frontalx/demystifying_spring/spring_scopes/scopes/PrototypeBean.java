package com.frontalx.demystifying_spring.spring_scopes.scopes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope("prototype")
public class PrototypeBean implements ScopeBean {

    private final String uuid = UUID.randomUUID().toString();

    public String getScopeName() {
        return "prototype@" + uuid;
    }

    @Override
    public String toString() {
        return "PrototypeBean@" + uuid + " | hashCode: " + this.hashCode();
    }
}
