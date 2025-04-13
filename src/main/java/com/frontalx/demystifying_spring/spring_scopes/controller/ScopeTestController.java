package com.frontalx.demystifying_spring.spring_scopes.controller;

import com.frontalx.demystifying_spring.spring_scopes.scopes.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
}

