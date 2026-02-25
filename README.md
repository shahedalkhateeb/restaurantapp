# Restaurant App – Android CI/CD

## Project Overview
This project is an Android Restaurant App that integrates:
- Google Maps
- Navigation Component
- Room Database
- View Binding
- Material Design

## CI/CD Pipeline

The GitHub Actions pipeline runs automatically on push and pull request to the master branch.

### Pipeline Stages:
- Lint check
- Unit Tests
- Build Debug APK
- Matrix testing on API 28, 30, 33
- Upload APK as artifact

## How to Run Locally

```bash
./gradlew lint
./gradlew testDebugUnitTest
./gradlew assembleDebug
