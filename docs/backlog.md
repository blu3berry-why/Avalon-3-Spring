# avalon-spring backlog

- **Bump jacoco 0.8.8 → ≥ 0.8.11** — 0.8.8 cannot instrument JDK ≥ 19 bytecode
  (class file major version ≥ 63), so the `jacocoTestReport` finalizer fails
  under JDK 17+/21 and takes `./gradlew test` down with it. Deferred from the
  Phase 0.5 springdoc work (out of scope there). See the retro
  `docs/superpowers/retros/2026-07-19-avalon-kmp-phase05-springdoc.md` in the
  parent deploy repo for context.
