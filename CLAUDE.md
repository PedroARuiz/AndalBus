# AndalBus — notes for Claude

Kotlin Multiplatform project (Android + iOS). This document describes the **actual** state of
the repo today; it gets updated at each step as we go, it is not an aspirational document. If
anything here doesn't match the code, the code wins.

## Architecture decision (agreed, in progress)

Native UI per platform (Jetpack Compose on Android, SwiftUI on iOS), sharing up to the
presentation layer (ViewModel + UiState). **This is not implemented yet** — today the project
is the KMP wizard scaffold plus quality/CI tooling. Don't assume Koin, SKIE, or a shared
ViewModel layer exist: they don't yet.

## Current modules

- `androidApp` — Android app (Jetpack Compose). Depends on `:sharedUI`.
- `sharedUI` — Android-only module (uses Compose Multiplatform plugins, but only compiles for
  Android today; not shared with iOS). Contains the wizard's demo UI (`App.kt`).
- `sharedLogic` — KMP module (Android + iOS). Contains the wizard's demo code (`Platform`,
  `Greeting`). This is where real shared logic should grow.
- `iosApp` — Xcode project with native SwiftUI, links `sharedLogic` via
  `embedAndSignAppleFrameworkForXcode` (no CocoaPods/SPM).
- `build-logic` — included build with convention plugins (see below).

## Convention plugins (`build-logic/convention`)

Four precompiled script plugins, registered but **intentionally empty** and not applied to
any module yet: `andalbus.kmp.domain`, `andalbus.kmp.data`, `andalbus.kmp.presentation`,
`andalbus.android.ui`. Once we create the first real module per layer, its matching convention
plugin is where the common configuration (KMP target setup, that layer's dependencies, etc.)
should go — don't duplicate it directly in the module's own `build.gradle.kts`.

## Commands

- Build + tests (everything except native iOS): `./gradlew build`
- Just what CI runs (no Xcode needed): `./gradlew ktlintCheck detekt :androidApp:build :sharedLogic:testAndroidHostTest :sharedUI:testAndroidHostTest`
- iOS tests (requires macOS/Xcode): `./gradlew :sharedLogic:iosSimulatorArm64Test`
- Format Kotlin: `./gradlew ktlintFormat`
- An empty `sdk.dir` in `local.properties` makes the build fail; if you hit that, export
  `ANDROID_HOME` instead of editing that file (it's gitignored, local to each machine).

## Code quality

- ktlint (14.2.0) and detekt (1.23.8) are applied to **all** subprojects centrally from the
  root `build.gradle.kts` (a `subprojects {}` block) — no need to declare them per module.
- Config: `.editorconfig` (ktlint) and `config/detekt/detekt.yml` (detekt,
  `buildUponDefaultConfig = true`).
- `.editorconfig` disables `ktlint_standard_function-naming` (`@Composable` functions are
  PascalCase) and all `ktlint_standard` rules under any `**/generated/**` path (generated
  code, e.g. Compose resource accessors — Gradle-side excludes don't work there because those
  tasks resolve their own file tree outside the standard mechanism; `.editorconfig` does work
  because ktlint consults it by path at lint time).

## CI

`.github/workflows/pr.yml`, a single job on `ubuntu-latest` (explicitly chosen over
`macos-latest` to avoid burning expensive runner minutes while there's no iOS to validate yet).
Runs ktlint + detekt + Android build/tests. **It does not compile anything iOS-related** —
that will be added as its own step when it's needed.

## Git / commits

- Commits in English, [Conventional Commits](https://www.conventionalcommits.org/)
  (`feat:`, `fix:`, `chore:`, `ci:`, `docs:`, etc.).
- No Claude co-author line.
- Ask before creating (or not creating) a branch when touching code while on `main`.
- **Ask before committing — don't commit automatically after finishing a step.**
- Before a PR: update `main` and rebase onto it; flag conflicts instead of resolving them
  silently.
- Work currently goes straight to `main` (explicit decision while the project is in its initial
  phase); branching will be revisited once there's more parallel work going on.
