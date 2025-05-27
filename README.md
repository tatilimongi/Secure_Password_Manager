# Secure Password Manager

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-22-blue.svg)](https://www.oracle.com/java/technologies/javase/22-relnote-issues.html)
[![Build with Maven](https://img.shields.io/badge/build-maven-brightgreen.svg)](https://maven.apache.org/)
[![Tests](https://github.com/tatilimongi/Secure_Password_Manager/actions/workflows/maven.yml/badge.svg)](https://github.com/tatilimongi/Secure_Password_Manager/actions)
[![Last Commit](https://img.shields.io/github/last-commit/tatilimongi/Secure_Password_Manager.svg)](https://github.com/tatilimongi/Secure_Password_Manager/commits/main)
[![Issues](https://img.shields.io/github/issues/tatilimongi/Secure_Password_Manager.svg)](https://github.com/tatilimongi/Secure_Password_Manager/issues)
[![Stars](https://img.shields.io/github/stars/tatilimongi/Secure_Password_Manager.svg)](https://github.com/tatilimongi/Secure_Password_Manager/stargazers)
[![Forks](https://img.shields.io/github/forks/tatilimongi/Secure_Password_Manager.svg)](https://github.com/tatilimongi/Secure_Password_Manager/network)

A secure, command-line password manager written in Java. It allows you to safely store, generate, and manage your credentials with strong encryption and modern security features.

## Features

- **Password Generation**: Option to automatically generate strong passwords with customizable length and character sets.
- **Password Security**: 
  - Encryption of stored passwords using industry-standard algorithms (AES-256).
  - Integration with HaveIBeenPwned API to check for compromised passwords.
  - Secure clipboard operations for password copying (clipboard is cleared after a short period).
- **User-Friendly Interface**: Command-line interface with clear menu options for adding, retrieving, updating, and deleting credentials.
- **Two-Factor Authentication (2FA)**: Support for TOTP (Time-based One-Time Password) for enhanced account security.
- **Master Password**: Protects access to all stored credentials.
- **Audit and Breach Check**: Easily check if your passwords have been exposed in known data breaches.

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

3. **Main menu options:**
   - Add new credential
   - Retrieve credential
   - Update credential
   - Delete credential
   - Generate strong password
   - Check password breach status
   - Configure 2FA (TOTP)
   - Exit

4. **Password Generation:**
   - Choose password length and character types (uppercase, lowercase, digits, symbols).

5. **Password Breach Check:**
   - Enter a password to check if it has been exposed in known data breaches using the HaveIBeenPwned API.

6. **Two-Factor Authentication (2FA):**
   - Set up TOTP for an extra layer of security. Store your TOTP secret securely.

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

