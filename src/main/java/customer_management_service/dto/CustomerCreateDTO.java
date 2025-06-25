package customer_management_service.dto;

import customer_management_service.validation.AgeMatchesBirthDate;
import customer_management_service.validation.ValidationConstants;
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
    @NotBlank(message = ValidationConstants.FIRST_NAME_REQUIRED)
    @Size(min = ValidationConstants.MIN_NAME_LENGTH, max = ValidationConstants.MAX_NAME_LENGTH, message = ValidationConstants.FIRST_NAME_LENGTH)
    @Pattern(regexp = ValidationConstants.NAME_PATTERN, message = ValidationConstants.NAME_PATTERN_MESSAGE)
    @Schema(example = "Juan")
    private String firstName;

    @NotBlank(message = ValidationConstants.LAST_NAME_REQUIRED)
    @Size(min = ValidationConstants.MIN_NAME_LENGTH, max = ValidationConstants.MAX_NAME_LENGTH, message = ValidationConstants.LAST_NAME_LENGTH)
    @Pattern(regexp = ValidationConstants.NAME_PATTERN, message = ValidationConstants.NAME_PATTERN_MESSAGE)
    @Schema(example = "Pérez")
    private String lastName;

    @NotNull(message = ValidationConstants.AGE_REQUIRED)
    @Min(value = ValidationConstants.MIN_AGE, message = ValidationConstants.AGE_MIN_MESSAGE)
    @Max(value = ValidationConstants.MAX_AGE, message = ValidationConstants.AGE_MAX_MESSAGE)
    @Schema(example = "30")
    private Integer age;
    
    @NotNull(message = ValidationConstants.BIRTH_DATE_REQUIRED)
    @Past(message = ValidationConstants.BIRTH_DATE_PAST)
    @Schema(example = "1994-01-01")
    private LocalDate birthDate;
} 