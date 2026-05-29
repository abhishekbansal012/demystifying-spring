package com.frontalx.demystifying_spring.spring_scopes.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frontalx.demystifying_spring.spring_scopes.scopes.ApplicationScopeBean;
import com.frontalx.demystifying_spring.spring_scopes.scopes.PrototypeBean;
import com.frontalx.demystifying_spring.spring_scopes.scopes.RequestBean;
import com.frontalx.demystifying_spring.spring_scopes.scopes.SessionBean;
import com.frontalx.demystifying_spring.spring_scopes.scopes.SingletonBean;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/scope")
public class ScopeTestController {

    @Autowired
    private SingletonBean singletonBean;

    @Autowired
    private ObjectFactory<PrototypeBean> prototypeBeanFactory;


    @Autowired
    private RequestBean requestBean;

    @Autowired
    private SessionBean sessionBean;

    @Autowired
    private ApplicationScopeBean applicationScopeBean;

    @GetMapping
    public Map<String, String> getScopes(HttpSession session) {
        Map<String, String> scopes = new LinkedHashMap<>();
        scopes.put("SingletonBean", singletonBean.getScopeName() + "@" + singletonBean.hashCode());
        scopes.put("RequestBean", requestBean.getScopeName() + "@" + requestBean.hashCode());
        scopes.put("SessionBean", sessionBean.getScopeName() + "@" + sessionBean.hashCode());
        scopes.put("ApplicationScopeBean", applicationScopeBean.getScopeName() + "@" + applicationScopeBean.hashCode());
        return scopes;
    }

    @GetMapping("/prototype-check")
    public List<String> checkPrototypeInstances() {
        List<String> results = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            PrototypeBean bean = prototypeBeanFactory.getObject();
            results.add(bean.toString());
        }

        return results;
    }

    @GetMapping("/request-scope")
    public String getRequestScopedId() {
        return requestBean.getScopeName();
    }

    @GetMapping("/session-scope")
    public String getSessionScopedId() {
        return "Session ID: " + sessionBean.getScopeName();
    }

    /**
     * Demonstrates shared mutable state in a singleton bean.
     * Every request increments the same counter — proving the bean is shared.
     * Try hitting this endpoint rapidly from multiple tabs/browsers to see the count climb.
     */
    @GetMapping("/singleton-state")
    public Map<String, Object> singletonSharedState() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("countBeforeThisRequest", singletonBean.getRequestCount());
        result.put("countAfterIncrement", singletonBean.incrementAndGet());
        result.put("beanIdentity", singletonBean.hashCode());
        result.put("message", "This counter never resets (unless the app restarts). All users share it.");
        return result;
    }
}

