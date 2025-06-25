package customer_management_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
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
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "The first name must be between 2 and 50 characters long")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "The first name can only contain letters and spaces")
    @Schema(example = "Juan")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "The last name must be between 2 and 50 characters long")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "The last name can only contain letters and spaces")
    @Schema(example = "Pérez")
    private String lastName;
    
    @NotNull(message = "Age is required")
    @Schema(example = "30")
    private Integer age;
    
    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Schema(example = "1994-01-01")
    private LocalDate birthDate;
    
    @Schema(example = "2024-12-25")
    private LocalDate estimatedEventDate;
    
    @Schema(example = "2024-01-15T10:30:00")
    private LocalDateTime creationDate;
    
    @Schema(example = "2024-01-15T14:45:00")
    private LocalDateTime updateDate;
} 