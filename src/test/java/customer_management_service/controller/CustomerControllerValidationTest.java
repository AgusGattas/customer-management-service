package customer_management_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

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
    void createCustomer_ShouldReturnBadRequest_WhenInvalidAgeAndBirthDate() throws Exception {
        // Arrange
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now().minusYears(25)); // 5 years difference

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.validation").value("Age does not match birth date"));
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
    void createCustomer_ShouldReturnBadRequest_WhenAgeTooHigh() throws Exception {
        // Arrange
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(200); // Age higher than maximum allowed
        dto.setBirthDate(LocalDate.now().minusYears(200));

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.age").value("Age must be less than or equal to 150"));
    }

    @Test
    void createCustomer_ShouldReturnBadRequest_WhenFutureBirthDate() throws Exception {
        // Arrange
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(30);
        dto.setBirthDate(LocalDate.now().plusYears(1)); // Future date

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.birthDate").value("Birth date must be in the past"));
    }
} 