package customer_management_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import customer_management_service.config.TestSecurityConfig;
import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import customer_management_service.dto.CustomerStatsDTO;
import customer_management_service.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(TestSecurityConfig.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private CustomerDTO customerDTO;
    private CustomerCreateDTO customerCreateDTO;
    private CustomerUpdateDTO customerUpdateDTO;
    private CustomerStatsDTO customerStatsDTO;

    @BeforeEach
    void setUp() {
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Doe");
        customerDTO.setAge(30);
        customerDTO.setBirthDate(LocalDate.of(1994, 1, 1));

        customerCreateDTO = new CustomerCreateDTO();
        customerCreateDTO.setFirstName("John");
        customerCreateDTO.setLastName("Doe");
        customerCreateDTO.setAge(30);
        customerCreateDTO.setBirthDate(LocalDate.of(1994, 1, 1));

        customerUpdateDTO = new CustomerUpdateDTO();
        customerUpdateDTO.setFirstName("Jane");
        customerUpdateDTO.setLastName("Smith");
        customerUpdateDTO.setAge(31);
        customerUpdateDTO.setBirthDate(LocalDate.of(1993, 1, 1));

        customerStatsDTO = new CustomerStatsDTO(30.0, 5.0, 10L);
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() throws Exception {
        // Arrange
        when(customerService.createCustomer(any(CustomerCreateDTO.class))).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void getAllCustomers_ShouldReturnListOfCustomers() throws Exception {
        // Arrange
        List<CustomerDTO> customers = Arrays.asList(customerDTO);
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Act & Assert
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].age").value(30));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() throws Exception {
        // Arrange
        when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() throws Exception {
        // Arrange
        CustomerDTO updatedCustomer = new CustomerDTO();
        updatedCustomer.setId(1L);
        updatedCustomer.setFirstName("Jane");
        updatedCustomer.setLastName("Smith");
        updatedCustomer.setAge(31);
        updatedCustomer.setBirthDate(LocalDate.of(1993, 1, 1));

        when(customerService.updateCustomer(anyLong(), any(CustomerUpdateDTO.class))).thenReturn(updatedCustomer);

        // Act & Assert
        mockMvc.perform(patch("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.age").value(31));
    }

    @Test
    void deleteCustomer_ShouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCustomerStats_ShouldReturnAllStats() throws Exception {
        // Arrange
        when(customerService.getCustomerStats()).thenReturn(customerStatsDTO);

        // Act & Assert
        mockMvc.perform(get("/api/customers/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").value(30.0))
                .andExpect(jsonPath("$.ageStandardDeviation").value(5.0))
                .andExpect(jsonPath("$.totalCustomers").value(10));
    }

    @Test
    void getAverageAge_ShouldReturnAverageAge() throws Exception {
        // Arrange
        when(customerService.getAverageAge()).thenReturn(30.0);

        // Act & Assert
        mockMvc.perform(get("/api/customers/stats/average-age"))
                .andExpect(status().isOk())
                .andExpect(content().string("30.0"));
    }

    @Test
    void getAgeStandardDeviation_ShouldReturnStandardDeviation() throws Exception {
        // Arrange
        when(customerService.getAgeStandardDeviation()).thenReturn(5.0);

        // Act & Assert
        mockMvc.perform(get("/api/customers/stats/age-standard-deviation"))
                .andExpect(status().isOk())
                .andExpect(content().string("5.0"));
    }
} 