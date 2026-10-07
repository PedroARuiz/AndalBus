# AndalBus

Kotlin Multiplatform project targeting Android and iOS.

The target architecture is native UI per platform (Jetpack Compose on Android, SwiftUI on
iOS), sharing code up to the presentation layer (ViewModel + UiState) — **not implemented
yet**. Today this is the KMP wizard scaffold plus git/CI/lint tooling. See
[CLAUDE.md](CLAUDE.md) for the up-to-date state of the project and its conventions.

## Modules

- [`androidApp`](./androidApp) — Android app (Jetpack Compose). Depends on `sharedUI`.
- [`sharedUI`](./sharedUI) — Android-only UI module (wizard demo screen).
- [`sharedLogic`](./sharedLogic) — Kotlin Multiplatform module (Android + iOS). Wizard demo
  code; this is where shared logic should grow.
- [`iosApp`](./iosApp) — Xcode project with native SwiftUI, links `sharedLogic`.
- [`build-logic`](./build-logic) — included build with convention plugins for future
  domain/data/presentation/UI modules (currently empty, not yet applied anywhere).

## Running the apps

- Android: `./gradlew :androidApp:assembleDebug`, or run from Android Studio.
- iOS: open [`iosApp/iosApp.xcodeproj`](./iosApp/iosApp.xcodeproj) in Xcode and run from there.

## Running tests

- Android (common + Android-specific): `./gradlew :sharedLogic:testAndroidHostTest :sharedUI:testAndroidHostTest`
- iOS (requires macOS/Xcode): `./gradlew :sharedLogic:iosSimulatorArm64Test`

## Code quality

- `./gradlew ktlintCheck` / `./gradlew ktlintFormat` — Kotlin style (config: `.editorconfig`).
- `./gradlew detekt` — static analysis (config: `config/detekt/detekt.yml`).

## CI

A single GitHub Actions workflow ([`.github/workflows/pr.yml`](.github/workflows/pr.yml)) runs
ktlint, detekt, and the Android build/tests on every PR. It does not build or test anything
iOS-related yet.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).
