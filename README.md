#  RestaurantApp

An Android application built with Java, featuring a complete CI/CD pipeline using GitHub Actions.

---

##  CI/CD Pipeline

### Pipeline Overview

This project uses GitHub Actions for automated CI/CD with two main jobs that run on every push and pull request to `master`:

```

### Stage Descriptions

| Stage | Tool | Description |
|-------|------|-------------|
| **Lint** | Android Lint | Checks code quality and potential bugs |
| **Unit Tests** | JUnit | Runs all unit tests with code coverage |
| **Code Coverage** | AGP built-in | Generates coverage report (`testCoverageEnabled`) |
| **Build APK** | Gradle | Compiles and uploads debug APK as artifact |
| **Instrumented Tests** | Espresso | Runs UI tests on emulators (API 28, 30, 33) |

---

### Artifacts Produced

Each pipeline run produces the following downloadable artifacts:

-  `lint-report` — HTML lint results
-  `unit-test-report` — JUnit test results
-  `coverage-report` — Code coverage report
-  `debug-apk` — Compiled debug APK
-  `espresso-report-api-{28,30,33}` — Instrumented test results per API level

---

### How to Run Locally

Make sure you have Android Studio installed and an emulator or device connected.

```bash
# Run Lint
./gradlew lint

# Run Unit Tests
./gradlew testDebugUnitTest

# Build Debug APK
./gradlew assembleDebug

# Run Instrumented Tests (requires connected device/emulator)
./gradlew connectedDebugAndroidTest
```

---

### Secrets Setup

No secrets are required for the basic pipeline.

For future bonus steps, add these under **Settings → Secrets and variables → Actions**:

| Secret Name | Purpose |
|-------------|---------|
| `FIREBASE_TOKEN` | Firebase App Distribution uploads |
| `KEYSTORE_FILE` | Release APK signing keystore |
| `KEYSTORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Key alias for signing |
| `KEY_PASSWORD` | Key password for signing |

---

### Pipeline Badge

Add this to the top of your README to show live pipeline status:

```markdown
![Android CI/CD](https://github.com/{YOUR_USERNAME}/restaurantapp/actions/workflows/android-ci.yml/badge.svg)
```

> Replace `{YOUR_USERNAME}` with your GitHub username.

---

## 🛠️ Tech Stack

| Technology | Usage |
|------------|-------|
| Java | Primary language |
| Android Jetpack | Navigation, ViewModel, LiveData, Room |
| Glide | Image loading |
| Google Maps SDK | Map integration |
| Espresso | UI testing |
| JUnit | Unit testing |
| GitHub Actions | CI/CD pipeline |


