package customer_management_service.validation;

import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AgeMatchesBirthDateValidatorTest {

    private AgeMatchesBirthDateValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AgeMatchesBirthDateValidator();
    }

    @Test
    void isValid_ShouldReturnTrue_WhenValidAgeAndBirthDate() {
        // Arrange
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now().minusYears(30));

        // Act
        boolean isValid = validator.isValid(dto, null);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenInvalidAgeAndBirthDate() {
        // Arrange
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now().minusYears(25)); // 5 años de diferencia

        // Act
        boolean isValid = validator.isValid(dto, null);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenAgeWithTolerance() {
        // Arrange - Caso edge donde la edad puede variar en ±1 año
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now().minusYears(30).minusDays(1)); // Justo antes del cumpleaños

        // Act
        boolean isValid = validator.isValid(dto, null);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenNullValues() {
        // Arrange
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setAge(null);
        dto.setBirthDate(null);

        // Act
        boolean isValid = validator.isValid(dto, null);

        // Assert
        assertTrue(isValid); // Debe ser válido si no hay valores para validar
    }

    @Test
    void isValid_ShouldReturnTrue_WhenCustomerUpdateDTO() {
        // Arrange
        CustomerUpdateDTO dto = new CustomerUpdateDTO();
        dto.setAge(25);
        dto.setBirthDate(LocalDate.now().minusYears(25));

        // Act
        boolean isValid = validator.isValid(dto, null);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenNullObject() {
        // Act
        boolean isValid = validator.isValid(null, null);

        // Assert
        assertTrue(isValid);
    }
} 