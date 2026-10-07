# Architecture

This describes the agreed target architecture. See [CLAUDE.md](CLAUDE.md) for what's actually
implemented today — most of this is **not built yet**.

## Approach

Clean Architecture with a unidirectional-flow presentation layer, shared across Android and
iOS up to — and including — the ViewModel. UI rendering is **not** shared: each platform has
its own native UI toolkit (Jetpack Compose / SwiftUI) consuming the same shared state. See
[ADR 0001](docs/adr/0001-shared-presentation-no-shared-ui.md) for why.

```
┌──────────────────┐        ┌──────────────────┐
│    androidApp      │        │      iosApp        │
│ (Jetpack Compose) │        │     (SwiftUI)      │
└─────────┬──────────┘        └─────────┬──────────┘
          │ viewModel()                  │ SKIE bridge
          └──────────────┬───────────────┘
                          │
                ┌─────────▼─────────┐
                │   presentation      │  ViewModel, UiState, UiEvent/Intent
                ├─────────────────────┤
                │       domain         │  Use cases, domain models, repository interfaces
                ├─────────────────────┤
                │        data          │  Repository impls, data sources, mappers
                └─────────────────────┘
                     sharedLogic (commonMain, with androidMain/iosMain actuals)
```

## Layers

- **domain** — pure Kotlin, no framework dependencies: use cases, domain models, repository
  interfaces.
- **data** — repository implementations, remote/local data sources, DTO↔domain mappers.
  Platform-specific pieces behind `expect`/`actual` or injected via Koin from
  `androidMain`/`iosMain`.
- **presentation** — `ViewModel` subclasses using the multiplatform
  `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel` class, each with an immutable
  `UiState`, a sealed `UiEvent`/`Intent` for user actions, and (where needed) a separate
  `UiEffect` stream for one-shot events like navigation.

These don't exist yet — today `sharedLogic` only has the wizard's demo code.

## Dependency injection

Koin. See [ADR 0002](docs/adr/0002-koin-for-di.md).

## iOS interop

SKIE generates idiomatic Swift `async`/`await` and `AsyncSequence` wrappers around the
suspend functions and `Flow`s exposed by `sharedLogic`, so SwiftUI can observe ViewModel state
without hand-written bridging code. See [ADR 0003](docs/adr/0003-skie-for-ios-interop.md).

## Module structure

A single `sharedLogic` module, organized by package (`domain`/`data`/`presentation`), is
enough for now — split into per-feature Gradle modules only once build times or team size make
it worth the overhead, not before. When the first real module per layer is created, it should
apply the matching (currently empty) convention plugin from `build-logic/convention`
(`andalbus.kmp.domain`, `andalbus.kmp.data`, `andalbus.kmp.presentation`,
`andalbus.android.ui`) rather than configuring itself from scratch.
