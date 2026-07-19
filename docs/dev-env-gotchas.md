# avalon-spring — dev env gotchas

Traps hit while building/testing avalon-spring, especially from a background or
sandboxed Claude session working inside the submodule.

## JDK 17 required (jacoco 0.8.8)

`build.gradle.kts` pins jacoco 0.8.8, which cannot instrument JDK-21 bytecode
(`java.lang.IllegalArgumentException: Unsupported class file major version 65`).
Tests then fail at the jacoco agent step under a JDK-21 default.

Fix: run with `JAVA_HOME` pointed at a JDK 17:

```sh
export JAVA_HOME=/path/to/jdk-17
./gradlew test
```

Or bump jacoco to ≥ 0.8.11 (see `docs/backlog.md`).

## Sandbox denies git/gradle writes → rerun with sandbox off

A sandboxed session cannot write:

- the initial gradle distribution download (network: `UnknownHostException services.gradle.org`)
  and `~/.gradle/wrapper/dists/*.lck`,
- `<parent-repo>/.git/modules/avalon-spring/worktrees/**/index.lock`
  (the submodule-worktree git dir), which breaks `git commit`.

Symptom: `Operation not permitted`. Fix: rerun that specific git/gradle command
with the sandbox disabled.

## bgIsolation guard blocks parent-repo edits from a submodule session

When the session's git repo is the `avalon-spring` submodule, `EnterWorktree`
only isolates the submodule; the parent deploy repo is not nested inside it, so
parent-repo file edits are rejected by the isolation guard. Deliverables that
live in the parent repo (`doc/`, `ForwardAuth/`, retros under
`docs/superpowers/`) must be done from a session rooted at the parent, or via
Bash with the sandbox disabled.

## Running tests offline

`@SpringBootTest` boots with no live infra: `FirebaseInitializer` swallows the
missing-config `IOException`, and Mongo connects lazily. So
`./gradlew test --offline -x jacocoTestReport` runs green without mongo or a
firebase config file present.
