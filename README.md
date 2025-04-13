# demystifying-spring
spring-java-conceptual-guide


| Scope       | Description                                  |
|-------------|----------------------------------------------|
| singleton   | One instance per Spring container (default). |
| prototype   | New instance every time it’s requested.      |
| request     | One instance per HTTP request (Web only).    |
| session     | One instance per HTTP session (Web only).    |
| application | One instance per ServletContext.             |
| websocket   | One instance per WebSocket session.          |



## Difference between Request Scope and Prototype


| Feature                            | @RequestScope                                 | @Scope("prototype")                                   |
|------------------------------------|-----------------------------------------------|-------------------------------------------------------|
| Scope Type                         | Per HTTP request                              | Per bean injection / retrieval                        |
| Lifecycle Managed By               | Spring container with web context             | Spring creates the bean, but YOU manage the lifecycle |
| Context Needed                     | Requires an active HTTP request               | Does not need a web context                           |
| Use Case                           | Web apps: request-specific state (e.g. forms) | When you want a new instance every time               |
| Lifecycle Ends When                | HTTP request ends                             | Immediately after it’s injected or retrieved          |
| Works with Autowired in Singleton? | No — needs proxy or ObjectFactory/Provider    | No — needs proxy or ObjectFactory/Provider            |
| Thread Safe?                       | Yes (one per request)                         | No (unless handled manually)                          |