package customer_management_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    @Schema(example = "1")
    private Long id;
    
    @Schema(example = "Juan")
    private String firstName;
    
    @Schema(example = "Pérez")
    private String lastName;
    
    @Schema(example = "30")
    private Integer age;
    
    @Schema(example = "1994-01-01")
    private LocalDate birthDate;
    
    @Schema(example = "2024-12-25")
    private LocalDate estimatedEventDate;
    
    @Schema(example = "2024-01-15T10:30:00")
    private LocalDateTime creationDate;
    
    @Schema(example = "2024-01-15T14:45:00")
    private LocalDateTime updateDate;
} 