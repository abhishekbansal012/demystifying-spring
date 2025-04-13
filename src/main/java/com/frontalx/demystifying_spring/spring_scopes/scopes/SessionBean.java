package com.frontalx.demystifying_spring.spring_scopes.scopes;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionBean implements ScopeBean {

    private final String uuid = UUID.randomUUID().toString();

    @Override
    public String getScopeName() {

        return "SessionBean@" + uuid;
    }
}
