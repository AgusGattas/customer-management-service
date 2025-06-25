package customer_management_service.controller;

import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import customer_management_service.dto.CustomerStatsDTO;
import customer_management_service.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for customer management")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer found"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update customer by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Get all customer statistics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<CustomerStatsDTO> getCustomerStats() {
        return ResponseEntity.ok(customerService.getCustomerStats());
    }

    @GetMapping("/stats/average-age")
    @Operation(summary = "Get average age of all customers")
    @ApiResponse(responseCode = "200", description = "Average age calculated successfully")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(customerService.getAverageAge());
    }

    @GetMapping("/stats/age-standard-deviation")
    @Operation(summary = "Get standard deviation of customer ages")
    @ApiResponse(responseCode = "200", description = "Standard deviation calculated successfully")
    public ResponseEntity<Double> getAgeStandardDeviation() {
        return ResponseEntity.ok(customerService.getAgeStandardDeviation());
    }
} 