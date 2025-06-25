package customer_management_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatsDTO {
    @Schema(example = "32.5", description = "Average age of all customers")
    private Double averageAge;
    
    @Schema(example = "8.2", description = "Standard deviation of customer ages")
    private Double ageStandardDeviation;
    
    @Schema(example = "150", description = "Total number of customers")
    private Long totalCustomers;
} 