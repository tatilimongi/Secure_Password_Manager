<div align="center">

# Secure Password Manager

<!-- ========== Project Info ========== -->

[![License: MIT](https://img.shields.io/badge/License-MIT-purple.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-22-blue.svg)](https://www.oracle.com/java/technologies/javase/22-relnote-issues.html)
[![Build with Maven](https://img.shields.io/badge/Build-Maven-brightgreen.svg)](https://maven.apache.org/)
![Maven Central](https://img.shields.io/badge/Maven-dependencies-blue?logo=apachemaven)
[![Dependencies](https://img.shields.io/librariesio/github/tatilimongi/Secure_Password_Manager)](https://libraries.io/github/tatilimongi/Secure_Password_Manager)

<!-- ========== Features ========== -->

![2FA](https://img.shields.io/badge/2FA-TOTP-green?style=flat)
![AES Encryption](https://img.shields.io/badge/Encryption-AES256-blue?style=flat)
![Terminal App](https://img.shields.io/badge/Interface-Terminal-informational?style=flat)

<!-- ========== Project Status ========== -->

![Project Status](https://img.shields.io/badge/status-active-brightgreen?style=flat)
![Contributions Welcome](https://img.shields.io/badge/contributions-welcome-orange?style=flat)

<!-- ========== GitHub Insights ========== -->

[![Last Commit](https://img.shields.io/github/last-commit/tatilimongi/Secure_Password_Manager.svg)](https://github.com/tatilimongi/Secure_Password_Manager/commits/main)
[![Commit Activity](https://img.shields.io/github/commit-activity/m/tatilimongi/Secure_Password_Manager.svg)](https://github.com/tatilimongi/Secure_Password_Manager/graphs/commit-activity)
[![Issues](https://img.shields.io/github/issues/tatilimongi/Secure_Password_Manager.svg)](https://github.com/tatilimongi/Secure_Password_Manager/issues)
[![Pull Requests](https://img.shields.io/github/issues-pr/tatilimongi/Secure_Password_Manager.svg)](https://github.com/tatilimongi/Secure_Password_Manager/pulls)

<!-- ========== Quality and Security ========== -->

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/1909c5e91ece446fbed19ae45659dae4)](https://app.codacy.com/gh/tatilimongi/Secure_Password_Manager/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![CodeQL Advanced](https://github.com/tatilimongi/Secure_Password_Manager/actions/workflows/codeql.yml/badge.svg)](https://github.com/tatilimongi/Secure_Password_Manager/actions/workflows/codeql.yml)
<img alt="Dependabot enabled" src="https://img.shields.io/badge/Dependabot-enabled-brightgreen?logo=dependabot" />

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=bugs)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=tatilimongi_Secure_Password_Manager&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=tatilimongi_Secure_Password_Manager)

A secure, command-line Password Manager written in Java. It allows you to safely store, generate, and manage your credentials with strong encryption and modern security features.

</div>

## Table of Contents
- [Features](#features)
- [Security Notes](#security-notes)
- [Prerequisites](#prerequisites)
- [Technologies Used](#technologies-used)
- [TOTP QR Code Generation](#totp-qr-code-generation)
- [Installation](#installation)
- [Usage](#usage)
- [Running Tests](#running-tests)
- [File Structure](#file-structure)
- [Contributing](#contributing)
- [License](#license)
- [Disclaimer](#disclaimer)

## Features

- **Password Generation**: Option to automatically generate strong passwords with customizable length and character sets.
  - **Password Breach Verification**: Generated passwords are checked against breach databases (like HaveIBeenPwned) to ensure they haven't been compromised.
- **Password Security**:
  - Encryption of stored passwords using industry-standard algorithms (AES-256).
  - Integration with HaveIBeenPwned API to check for compromised passwords.
  - Secure clipboard operations for password copying (clipboard is cleared after a short period).
- **User-Friendly Interface**: Command-line interface with clear menu options for adding, retrieving, updating, and deleting credentials.
- **Two-Factor Authentication (2FA)**: Support for TOTP (Time-based One-Time Password) for enhanced account security.
- **Master Password**: Protects access to all stored credentials.
- **Audit and Breach Check**: Easily check if your passwords have been exposed in known data breaches.

## Security Notes

- **Advanced Encryption**: All stored credentials are secured using AES-GCM for authenticated encryption.
- **Input Sanitization**: User-provided input is rigorously validated to prevent injection attacks or unsafe inputs.
- **Sensitive Data Cleanup**: Mechanisms are in place to clear encryption keys and sensitive data from memory when the application shuts down.
- The master password is never stored; only a hash is kept using BCrypt.
- Clipboard operations are cleared after a short timeout to prevent leaks.
- Passwords are never logged or displayed in plain text.

## Prerequisites

- Java Development Kit (JDK) 22 or higher
- Maven 3.6.0 or higher
- Git (optional, for version control)

## Technologies Used

- **Java 22**: Core programming language
- **Maven**: Project management and build tool
- **Dependencies**:
  - [jBCrypt](https://www.mindrot.org/projects/jBCrypt/): For password hashing
  - [Gson](https://github.com/google/gson): For JSON serialization
  - [Apache Commons Codec](https://commons.apache.org/proper/commons-codec/): For encoding/decoding utilities
  - [JUnit 5](https://junit.org/junit5/): For unit testing

## TOTP QR Code Generation

To easily set up Two-Factor Authentication (2FA) with authenticator apps (like Google Authenticator, Microsoft Authenticator, or Authy), you can convert your TOTP URL into a QR code using one of the following free online tools:

- [https://www.qr-code-generator.com/](https://www.qr-code-generator.com/)
- [https://www.the-qrcode-generator.com/](https://www.the-qrcode-generator.com/)
- [https://www.qrstuff.com/](https://www.qrstuff.com/)
- [https://www.unitag.io/qrcode](https://www.unitag.io/qrcode)
- [https://www.google.com/chart?cht=qr&chs=300x300&chl=YOUR_TOTP_URL](https://www.google.com/chart?cht=qr&chs=300x300&chl=YOUR_TOTP_URL) (replace `YOUR_TOTP_URL` with your actual TOTP URL)

**Instructions:**
1. Copy your TOTP URL (e.g., `otpauth://totp/YourApp:username?secret=BASE32SECRET&issuer=YourApp`).
2. Paste it into one of the QR code generator websites above.
3. Scan the generated QR code with your authenticator app.

## Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/tatilimongi/Secure_Password_Manager.git
   cd Secure_Password_Manager
   ```

2. **Build the project with Maven:**
   ```sh
   mvn clean package
   ```
   The executable JAR will be generated in the `target/` directory.

## Usage

1. **Run the application:**
   ```sh
   java -jar target/secure-password-manager-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

2. **First-time setup:**
   - You will be prompted to create a master password. This password is required to access your credentials.

3. **Two-Factor Authentication (2FA):**
   - Set up TOTP for an extra layer of security. Store your TOTP secret securely.

4. **Main menu options:**
   - List all credentials
   - Add new credential
   - Delete a credential
   - Copy password to clipboard
   - Check if any password has been compromised
   - Exit

5. **Password Generation:**
   - Choose password length and character types (uppercase, lowercase, digits, symbols).

6. **Password Breach Check:**
   - Enter a password to check if it has been exposed in known data breaches using the HaveIBeenPwned API.

## Security Notes

- All credentials are encrypted at rest using AES-256.
- The master password is never stored; only a hash is kept using BCrypt.
- Clipboard operations are cleared after a short timeout to prevent leaks.
- Passwords are never logged or displayed in plain text.

## Running Tests

To run all unit tests:
```sh
mvn test
```

## File Structure

- `src/main/java/` - Application source code
- `src/test/java/` - Unit tests
- `lib/` - External libraries (if any)
- `target/` - Compiled binaries and packaged JARs

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request. For major changes, open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Disclaimer

This project is for educational purposes. Use at your own risk. Always back up your credentials and never share your master password.