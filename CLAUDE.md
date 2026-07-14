# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build the entire project
./gradlew build

# Run tests only
./gradlew test

# Run a single test class
./gradlew :modules:shared:test --tests "com.cn.IpUtilTest"

# Run the admin backend (port 9090, Thymeleaf + form login)
./gradlew :apps:admin-app:bootRun

# Run the web API (port 8070, JWT + Swagger)
./gradlew :apps:web-app:bootRun

# Vue frontend dev server
cd apps/web-client && npm run dev
```

Subprojects apply Spring Boot + dependency-management plugins via root `build.gradle` `subprojects` block. Modules/libs produce plain JARs (`jar.enabled = true, bootJar.enabled = false`); only apps produce executable Spring Boot JARs.

## Profiles

- **admin-app** default profile is `local`: H2 in-memory, `spring.docker.compose.enabled=true` auto-starts containers from root `docker-compose.yml`.
- **web-app** connects MySQL by default, no profile switching.
- **Production** (`application-prod.yml`): MySQL, DDL generation disabled.
- Sensitive config uses env vars with dev-only fallback defaults (`${MAIL_USERNAME:}`, `${JWT_SECRET:dev-only-change-me}`).
- Jasypt: both `main()` methods set `jasypt.encryptor.password` from `ConfigEnum.JASYPT_ENCRYPTOR` before Spring starts, enabling `ENC(...)` values in YAML.

## Architecture: Three-Layer Multi-Module

```
apps/                         # Runnable Spring Boot JARs — entry points
├── admin-app/                # Admin backend: Thymeleaf, form login, cookie/session
├── web-app/                  # Public API: JWT, stateless, Swagger
└── web-client/               # Vue 3 + Vite + Element Plus (separate build)

modules/                      # Business logic — plain JARs
├── shared/                   # Zero Spring deps: enums, Result<T>, GlobalException, POJOs, utilities
├── user/                     # Users, signup/activation, QQ/WeChat social login
├── content/                  # Articles, carousels, classify
├── relation/                 # Follows, favorites
├── notification/             # Notices CRUD
├── search/                   # Elasticsearch book search
├── security/                 # Admin RBAC (Manager, Role, Permission), login logs
└── file/                     # File management (placeholder)

infrastructure/               # Tech adapters — Spring Boot auto-config
├── persistence/              # JPA entities, repositories, Druid pool, @EntityListeners auditing
├── messaging/                # RabbitMQ + WebSocket
├── cache/                    # Redis
├── oss/                      # S3-compatible storage (Aliyun OSS)
└── observability/            # Logging/tracing (placeholder)
```

**Dependency direction (strict)**: `apps → modules → infrastructure → modules/shared`
- Modules can depend on each other (e.g., `content → search`).
- Infrastructure modules depend on `modules/shared`; cross-infrastructure deps kept minimal.
- No reverse dependencies — modules never depend on apps.

## Authentication Models

### admin-app — session-based, server-rendered
- Form login at `/login`, Thymeleaf views, cookie + remember-me.
- `MyAuthorizationManager` does fine-grained URL access control.
- `MyAuthenticationProvider` authenticates against `ManagerService`.
- Permitted anonymously: `/captcha`, static resources, `/druid/*`, `/h2-console/*`.

### web-app — stateless JWT
- POST `/login` with JSON body → `JwtLoginFilter` → JWT in `Authorization` header.
- `JwtAuthenticationFilter` validates Bearer token on every request via Hutool JWT.
- Two auth providers: `DaoAuthenticationProvider` (username/password) and `SocialAuthenticationProvider` (QQ/WeChat OAuth code).
- `User` entity directly implements `UserDetails` — its fields are serialized into the JWT payload.
- Token config: `jwt.secret` / `jwt.expiration` in `application.yml`.

## Key Patterns

- **Response format**: `Result<T>` (`modules/shared`) — `Result.success(data)` / `Result.failure(RestCode)`. `RestCode` enum defines all error codes.
- **Exceptions**: `GlobalException(code, msg)` → caught by per-app `@ControllerAdvice` → mapped to `Result.failure()`.
- **Persistence**: entities extend `AbstractDateAudit` (auto createdAt/updatedAt) or `AbstractUserDateAudit` (adds createdBy/updatedBy via Spring Security auditor). Repositories use `Specification<T>` for dynamic queries. `@SQLRestriction("available=true")` for soft-delete-like filtering.
- **Messaging**: two RabbitMQ queues — `notify-queue` (direct) and `delay-queue` (delayed-message plugin, used for 3-hour activation expiry). `RabbitTemplate` uses JSON message converter with confirm/return callbacks.
- **OSS config**: `OssConfigProperties` is `@ConfigurationProperties(prefix = "oss.config")` with `@Validated`, bound from YAML. Supports S3-compatible storage with configurable region, endpoint, bucket.

## Docker Compose (dev)

Root `docker-compose.yml`: Elasticsearch 9.2.2, Kibana 9.2.2, Logstash 9.2.2, Redis, RabbitMQ 4.2.1-management. Admin-app's `local` profile has `spring.docker.compose.enabled=true`, so `bootRun` auto-starts containers.

After first RabbitMQ container creation, install the delayed-message-exchange plugin manually (see README).

## Web Client

`apps/web-client/` — Vue 3 + Vite + Element Plus + Pinia. Uses `sockjs-client` + `stompjs` for WebSocket. Not part of Gradle build.

## Stack Summary

| Layer | Tech |
|-------|------|
| Framework | Spring Boot 4.1.0, Java 25 |
| Security | Spring Security (form + JWT) |
| ORM | Spring Data JPA / Hibernate |
| DB | MySQL (prod), H2 (dev) |
| Pool | Alibaba Druid |
| Cache | Redis |
| MQ | RabbitMQ (delayed-message plugin) |
| Search | Elasticsearch 9.x |
| Storage | Alibaba OSS / S3 |
| Admin UI | Thymeleaf + Layui |
| Web UI | Vue 3 + Element Plus + Vite |
| API docs | SpringDoc OpenAPI (web-app) |
| Utils | Hutool 5.x, Guava |
| Monitoring | Spring Boot Actuator |