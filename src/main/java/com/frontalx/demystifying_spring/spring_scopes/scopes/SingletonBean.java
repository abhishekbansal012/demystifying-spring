package com.frontalx.demystifying_spring.spring_scopes.scopes;

import org.springframework.stereotype.Component;

@Component
public class SingletonBean implements ScopeBean{

    @Override
    public String getScopeName() {
        return "singleton";
    }
}
