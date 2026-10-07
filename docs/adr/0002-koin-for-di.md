# 2. Koin for dependency injection

## Status

Accepted (decision made; not implemented yet - see [ARCHITECTURE.md](../../ARCHITECTURE.md)).

## Context

The shared module needs a DI mechanism that works across Kotlin/JVM (Android) and
Kotlin/Native (iOS), and that both app entry points can bootstrap with platform-specific
bindings (Android `Context`, etc.).

## Decision

Use Koin (`io.insert-koin`). Runtime DI resolution, mature KMP support, small API surface.

## Consequences

- No compile-time safety on the dependency graph (vs. a KSP-based alternative like
  Kotlin-inject) - mistakes surface at `startKoin()` time or on first resolution, not at
  compile time.
- Each platform will call a single shared bootstrap entry point (`initKoin`) once at startup
  with its own platform-specific `KoinAppDeclaration`.
