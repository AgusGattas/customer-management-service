package customer_management_service.dto;

import customer_management_service.validation.AgeMatchesBirthDate;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@AgeMatchesBirthDate
public class CustomerCreateDTO {
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
    @Min(value = 0, message = "Age must be greater than or equal to 0")
    @Max(value = 150, message = "Age must be less than or equal to 150")
    @Schema(example = "30")
    private Integer age;
    
    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Schema(example = "1994-01-01")
    private LocalDate birthDate;
} 