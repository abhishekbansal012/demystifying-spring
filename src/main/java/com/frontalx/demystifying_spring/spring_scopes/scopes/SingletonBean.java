package com.frontalx.demystifying_spring.spring_scopes.scopes;

import org.springframework.stereotype.Component;

@Component
public class SingletonBean implements ScopeBean {

    private int requestCount = 0;

    @Override
    public String getScopeName() {
        return "singleton";
    }

    /**
     * Deliberately NOT thread-safe.
     * Demonstrates that singleton state is shared across all requests/users.
     * In production, this would be a bug — singletons should be stateless or use synchronization.
     */
    public int incrementAndGet() {
        return ++requestCount;
    }

    public int getRequestCount() {
        return requestCount;
    }
}
