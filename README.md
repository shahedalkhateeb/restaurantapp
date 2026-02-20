# Android Project CI/CD Pipeline

##  Pipeline Overview
This project uses a GitHub Actions workflow to automate the CI/CD process for an Android project.  
The workflow ensures code quality, runs tests, generates coverage reports, and builds the debug APK automatically.

##  Pipeline Stages

1. **Checkout code**
   - Pulls the latest code from GitHub.

2. **Set up JDK**
   - Sets up Java 17 using Temurin distribution.

3. **Cache Gradle**
   - Caches Gradle dependencies for faster builds.

4. **Build Debug APK**
   - Compiles the Android app into a debug APK.

5. **Lint & Detekt**
   - Runs static code analysis and checks code style.

6. **Unit & Instrumented Tests**
   - Executes unit tests and instrumented tests (Espresso).

7. **Jacoco Coverage**
   - Generates code coverage reports for unit tests.

8. **Upload Artifacts**
   - Uploads debug APK, test reports, and Jacoco coverage reports to GitHub Actions artifacts.

##  How to Run Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/username/your-project.git
   cd your-project
