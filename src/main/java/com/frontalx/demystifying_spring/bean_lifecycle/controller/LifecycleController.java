package com.frontalx.demystifying_spring.bean_lifecycle.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frontalx.demystifying_spring.bean_lifecycle.beans.AnnotationLifecycleBean;
import com.frontalx.demystifying_spring.bean_lifecycle.beans.BeanAwareDemo;
import com.frontalx.demystifying_spring.bean_lifecycle.beans.CustomBeanPostProcessor;
import com.frontalx.demystifying_spring.bean_lifecycle.beans.CustomInitMethodBean;
import com.frontalx.demystifying_spring.bean_lifecycle.beans.FullLifecycleBean;
import com.frontalx.demystifying_spring.bean_lifecycle.beans.InterfaceLifecycleBean;

@RestController
@RequestMapping("/lifecycle")
public class LifecycleController {

    @Autowired
    private AnnotationLifecycleBean annotationBean;

    @Autowired
    private InterfaceLifecycleBean interfaceBean;

    @Autowired
    private FullLifecycleBean fullLifecycleBean;

    @Autowired
    private BeanAwareDemo beanAwareDemo;

    @Autowired
    private CustomBeanPostProcessor beanPostProcessor;

    @Autowired
    private CustomInitMethodBean customInitMethodBean;

    /**
     * Shows lifecycle events for the annotation-based bean (@PostConstruct / @PreDestroy).
     */
    @GetMapping("/annotations")
    public Map<String, Object> annotationLifecycle() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("bean", "AnnotationLifecycleBean");
        result.put("mechanism", "@PostConstruct and @PreDestroy");
        result.put("events", annotationBean.getLifecycleEvents());
        result.put("note", "@PreDestroy will fire on app shutdown — check console logs");
        return result;
    }

    /**
     * Shows lifecycle events for the interface-based bean (InitializingBean / DisposableBean).
     */
    @GetMapping("/interfaces")
    public Map<String, Object> interfaceLifecycle() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("bean", "InterfaceLifecycleBean");
        result.put("mechanism", "InitializingBean.afterPropertiesSet() and DisposableBean.destroy()");
        result.put("events", interfaceBean.getLifecycleEvents());
        result.put("note", "DisposableBean.destroy() will fire on app shutdown — check console logs");
        return result;
    }

    /**
     * Shows the FULL lifecycle order when all mechanisms are combined in one bean.
     */
    @GetMapping("/full-order")
    public Map<String, Object> fullLifecycleOrder() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("bean", "FullLifecycleBean");
        result.put("mechanism", "All lifecycle callbacks combined");
        result.put("initializationOrder", fullLifecycleBean.getLifecycleEvents());
        result.put("destructionOrder", List.of(
                "4. @PreDestroy (fires first)",
                "5. DisposableBean.destroy() (fires second)",
                "→ Check console on app shutdown to see both"
        ));
        return result;
    }

    /**
     * Shows the Aware interface callbacks — how a bean learns about its environment.
     */
    @GetMapping("/aware")
    public Map<String, Object> awareCallbacks() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("bean", "BeanAwareDemo");
        result.put("registeredBeanName", beanAwareDemo.getBeanName());
        result.put("awarenessCallbacks", beanAwareDemo.getAwarenessEvents());
        result.put("note", "Aware interfaces are called after constructor but before @PostConstruct");
        return result;
    }

    /**
     * Shows which beans were intercepted by our custom BeanPostProcessor.
     */
    @GetMapping("/post-processor")
    public Map<String, Object> beanPostProcessorLog() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("bean", "CustomBeanPostProcessor");
        result.put("mechanism", "BeanPostProcessor.postProcessBeforeInitialization / postProcessAfterInitialization");
        result.put("interceptedBeans", beanPostProcessor.getProcessingLog());
        result.put("note", "BEFORE runs before @PostConstruct; AFTER runs after @PostConstruct/afterPropertiesSet()");
        return result;
    }

    /**
     * Shows the complete bean lifecycle order as a reference.
     */
    @GetMapping("/complete-order")
    public Map<String, Object> completeLifecycleReference() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", "Complete Spring Bean Lifecycle Order");
        result.put("initialization", List.of(
                "1. Bean class instantiated (constructor)",
                "2. Dependencies injected (@Autowired / setter / field)",
                "3. BeanNameAware.setBeanName()",
                "4. BeanFactoryAware.setBeanFactory()",
                "5. ApplicationContextAware.setApplicationContext()",
                "6. BeanPostProcessor.postProcessBeforeInitialization()",
                "7. @PostConstruct",
                "8. InitializingBean.afterPropertiesSet()",
                "9. Custom init-method (if configured in @Bean)",
                "10. BeanPostProcessor.postProcessAfterInitialization()",
                "→ Bean is ready for use"
        ));
        result.put("destruction", List.of(
                "1. @PreDestroy",
                "2. DisposableBean.destroy()",
                "3. Custom destroy-method (if configured in @Bean)"
        ));
        return result;
    }

    /**
     * Shows @Bean(initMethod, destroyMethod) — custom lifecycle methods without annotations.
     */
    @GetMapping("/custom-init-method")
    public Map<String, Object> customInitMethod() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("bean", "CustomInitMethodBean");
        result.put("mechanism", "@Bean(initMethod = \"init\", destroyMethod = \"cleanup\")");
        result.put("status", customInitMethodBean.getStatus());
        result.put("events", customInitMethodBean.getLifecycleEvents());
        result.put("note", "cleanup() will fire on app shutdown — check console logs");
        result.put("use_case", "Useful for third-party classes you can't annotate with @PostConstruct/@PreDestroy");
        return result;
    }
}
