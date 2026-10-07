# 3. SKIE for Kotlin → Swift interop

## Status

Accepted (decision made; not implemented yet - see [ARCHITECTURE.md](../../ARCHITECTURE.md)).

## Context

SwiftUI needs to consume `sharedLogic`'s suspend functions and `Flow`s (ViewModel state) in
an idiomatic way. Plain Kotlin/Native Objective-C interop exposes suspend functions as
completion-handler closures and `Flow` as a raw platform type, which is awkward to use from
Swift and easy to get wrong (e.g. forgetting to cancel a collection).

## Decision

Apply the SKIE Gradle plugin to `sharedLogic`. It post-processes the generated framework to
expose `async`/`await` functions and `AsyncSequence`-based `Flow` wrappers directly, with no
hand-written bridging code.

## Consequences

- One more Gradle plugin/build-time dependency on `sharedLogic`.
- Alternative considered: KMP-NativeCoroutines (more manual control, more boilerplate per
  function/property). Revisit if SKIE's generated API doesn't cover a specific case well.
