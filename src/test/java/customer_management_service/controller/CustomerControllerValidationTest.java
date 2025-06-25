package customer_management_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCustomer_ShouldReturnOk_WhenValidAgeAndBirthDate() throws Exception {
        // Arrange
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now().minusYears(30));

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Pérez"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    @DisplayName("Should return 400 when age does not match birth date")
    void shouldReturn400WhenAgeDoesNotMatchBirthDate() throws Exception {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(30));

        // When & Then
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCustomer_ShouldReturnBadRequest_WhenInvalidAgeAndBirthDate() throws Exception {
        // Arrange
        CustomerUpdateDTO dto = new CustomerUpdateDTO();
        dto.setAge(40);
        dto.setBirthDate(LocalDate.now().minusYears(35)); // 5 years difference

        // Act & Assert
        mockMvc.perform(patch("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.validation").value("Age does not match birth date"));
    }

    @Test
    @DisplayName("Should return 400 when age is too high")
    void shouldReturn400WhenAgeIsTooHigh() throws Exception {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(200);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(25)); // 5 years difference

        // When & Then
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.age").value("Age must be less than or equal to 150"));
    }

    @Test
    @DisplayName("Should return 400 when age is too low")
    void shouldReturn400WhenAgeIsTooLow() throws Exception {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(5);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(35)); // 5 years difference

        // When & Then
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.validation").value("Age does not match birth date"));
    }

    @Test
    @DisplayName("Should return 400 when age is negative")
    void shouldReturn400WhenAgeIsNegative() throws Exception {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(-5);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).minusYears(200));

        // When & Then
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.age").value("Age must be greater than or equal to 0"));
    }

    @Test
    @DisplayName("Should return 400 when birth date is in the future")
    void shouldReturn400WhenBirthDateIsInTheFuture() throws Exception {
        // Given
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now(ZoneOffset.UTC).plusYears(1)); // Future date

        // When & Then
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.birthDate").value("Birth date must be in the past"));
    }
} 