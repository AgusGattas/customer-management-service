package customer_management_service.integration;

import customer_management_service.config.TestSecurityConfig;
import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerDTO;
import customer_management_service.dto.CustomerStatsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
@Import(TestSecurityConfig.class)
public class CustomerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void completeCustomerFlow_ShouldWorkEndToEnd() throws Exception {
        // Arrange
        CustomerCreateDTO createDTO = new CustomerCreateDTO();
        createDTO.setFirstName("Juan");
        createDTO.setLastName("Pérez");
        createDTO.setAge(30);
        createDTO.setBirthDate(LocalDate.of(1994, 1, 1));

        // Act & Assert - Create customer
        String createResponse = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Pérez"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.estimatedEventDate").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CustomerDTO createdCustomer = objectMapper.readValue(createResponse, CustomerDTO.class);

        // Act & Assert - Get customer by ID
        mockMvc.perform(get("/api/customers/" + createdCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdCustomer.getId()))
                .andExpect(jsonPath("$.firstName").value("Juan"));

        // Act & Assert - Get all customers
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Juan"));

        // Act & Assert - Get statistics
        mockMvc.perform(get("/api/customers/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").value(30.0))
                .andExpect(jsonPath("$.totalCustomers").value(1));

        // Act & Assert - Delete customer
        mockMvc.perform(delete("/api/customers/" + createdCustomer.getId()))
                .andExpect(status().isNoContent());

        // Act & Assert - Verify customer is deleted
        mockMvc.perform(get("/api/customers/" + createdCustomer.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void validationErrors_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        // Arrange - Test invalid age
        CustomerCreateDTO invalidDTO = new CustomerCreateDTO();
        invalidDTO.setFirstName("Juan");
        invalidDTO.setLastName("Pérez");
        invalidDTO.setAge(200); // Invalid age
        invalidDTO.setBirthDate(LocalDate.of(1994, 1, 1));

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        // Arrange - Test invalid name format
        CustomerCreateDTO invalidNameDTO = new CustomerCreateDTO();
        invalidNameDTO.setFirstName("Juan123"); // Invalid name format
        invalidNameDTO.setLastName("Pérez");
        invalidNameDTO.setAge(30);
        invalidNameDTO.setBirthDate(LocalDate.of(1994, 1, 1));

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidNameDTO)))
                .andExpect(status().isBadRequest());
    }
} 