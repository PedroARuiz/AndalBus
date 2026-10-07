# Contributing

See [CLAUDE.md](CLAUDE.md) for the project's current state, module layout and conventions in
detail. This file is the short process checklist.

## Branching

Work currently goes straight to `main` (explicit decision while the project is in its initial
phase). Once that's revisited, branches should be short-lived and per feature.

## Commits

[Conventional Commits](https://www.conventionalcommits.org/), in English:
`feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `ci:`, `chore:`.

## Before opening a PR

1. Update `main` and rebase your branch onto it. Flag conflicts instead of resolving them
   silently.
2. Run the same checks CI runs: `./gradlew ktlintCheck detekt :androidApp:build :sharedLogic:testAndroidHostTest :sharedUI:testAndroidHostTest`.

## Code quality

- `./gradlew ktlintFormat` before committing Kotlin changes.
- `./gradlew detekt` must be clean (config in `config/detekt/detekt.yml`).
