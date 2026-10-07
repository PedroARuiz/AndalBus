# 1. Share domain/data/presentation, not UI

## Status

Accepted (decision made; not implemented yet - see [ARCHITECTURE.md](../../ARCHITECTURE.md)).

## Context

The project is Kotlin Multiplatform targeting Android and iOS. Compose Multiplatform would
let the UI itself be shared and rendered on both platforms. The wizard-generated project
includes a `sharedUI` module built on Compose Multiplatform, though in its current form it
only ever compiles for Android.

## Decision

UI will be native per platform: Jetpack Compose on Android, SwiftUI on iOS. Domain, data and
the presentation layer (ViewModel + UiState) will be shared in `sharedLogic`.

## Consequences

- Each platform gets full native look-and-feel, platform APIs and tooling (previews,
  accessibility, platform widgets) without compromise.
- Business and presentation logic (state management, use cases, data access) is written once.
- UI code and UI tests are duplicated across platforms - accepted trade-off.
- iOS needs a Kotlin→Swift interop layer for the shared ViewModel/Flow API (see
  [ADR 0003](0003-skie-for-ios-interop.md)).
- The current `sharedUI` module's role (keep as-is, rename, or fold into `androidApp`) is not
  yet decided and should be revisited when this is actually implemented.
