package customer_management_service.validation;

import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class AgeMatchesBirthDateValidatorTest {

    private AgeMatchesBirthDateValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AgeMatchesBirthDateValidator();
    }

    @Test
    @DisplayName("Should be valid when age matches birth date")
    void shouldBeValidWhenAgeMatchesBirthDate() {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(30));

        // When
        boolean isValid = validator.isValid(dto, null);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should be invalid when age does not match birth date")
    void shouldBeInvalidWhenAgeDoesNotMatchBirthDate() {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(25)); // 5 años de diferencia

        // When
        boolean isValid = validator.isValid(dto, null);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should be valid when age is within tolerance (±1 year)")
    void shouldBeValidWhenAgeIsWithinTolerance() {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(30).minusDays(1)); // Justo antes del cumpleaños

        // When
        boolean isValid = validator.isValid(dto, null);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should be valid when only age is provided")
    void shouldBeValidWhenOnlyAgeIsProvided() {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(30);
        dto.setBirthDate(null);

        // When
        boolean isValid = validator.isValid(dto, null);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should be valid when only birth date is provided")
    void shouldBeValidWhenOnlyBirthDateIsProvided() {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(null);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(25));

        // When
        boolean isValid = validator.isValid(dto, null);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should be valid when both values are null")
    void shouldBeValidWhenBothValuesAreNull() {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(null);
        dto.setBirthDate(null);

        // When
        boolean isValid = validator.isValid(dto, null);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should be valid when dto is null")
    void shouldBeValidWhenDtoIsNull() {
        // When
        boolean isValid = validator.isValid(null, null);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should work with CustomerUpdateDTO")
    void shouldWorkWithCustomerUpdateDTO() {
        // Given
        CustomerUpdateDTO dto = new CustomerUpdateDTO();
        dto.setAge(25);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(25));

        // When
        boolean isValid = validator.isValid(dto, null);

        // Then
        assertTrue(isValid);
    }
} 