package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordGeneratorTest {

    @Test
    @DisplayName("Should generate password with specified length")
    void testPasswordLength() {
        int expectedLength = 12;
        String password = PasswordGenerator.generate(expectedLength, true, true, true, true);
        assertEquals(expectedLength, password.length());
    }

    @Test
    @DisplayName("Should throw exception for invalid length")
    void testInvalidLength() {
        assertThrows(IllegalArgumentException.class, () ->
            PasswordGenerator.generate(0, true, true, true, true)
        );
        
        assertThrows(IllegalArgumentException.class, () ->
            PasswordGenerator.generate(-1, true, true, true, true)
        );
    }

    @Test
    @DisplayName("Should throw exception when no character types selected")
    void testNoCharacterTypesSelected() {
        assertThrows(IllegalArgumentException.class, () ->
            PasswordGenerator.generate(10, false, false, false, false)
        );
    }

    @Nested
    @DisplayName("Character type inclusion tests")
    class CharacterTypeTests {
        
        @Test
        @DisplayName("Should include only uppercase letters")
        void testUppercaseOnly() {
            String password = PasswordGenerator.generate(10, true, false, false, false);
            assertTrue(password.matches("^[A-Z]{10}$"));
        }

        @Test
        @DisplayName("Should include only lowercase letters")
        void testLowercaseOnly() {
            String password = PasswordGenerator.generate(10, false, true, false, false);
            assertTrue(password.matches("^[a-z]{10}$"));
        }

        @Test
        @DisplayName("Should include only numbers")
        void testNumbersOnly() {
            String password = PasswordGenerator.generate(10, false, false, true, false);
            assertTrue(password.matches("^[0-9]{10}$"));
        }

        @Test
        @DisplayName("Should include only symbols")
        void testSymbolsOnly() {
            String password = PasswordGenerator.generate(10, false, false, false, true);
            assertTrue(password.matches("^[!@#$%&*()\\-_=+\\[\\]{}]{10}$"));
        }
    }

    @Nested
    @DisplayName("Multiple character types combination tests")
    class CombinationTests {
        
        @Test
        @DisplayName("Should generate different passwords on multiple calls")
        void testRandomness() {
            String password1 = PasswordGenerator.generate(10, true, true, true, true);
            String password2 = PasswordGenerator.generate(10, true, true, true, true);
            assertNotEquals(password1, password2);
        }

        @ParameterizedTest
        @ValueSource(ints = {8, 16, 32, 64})
        @DisplayName("Should handle various password lengths")
        void testVariousLengths(int length) {
            String password = PasswordGenerator.generate(length, true, true, true, true);
            assertEquals(length, password.length());
        }
    }
}