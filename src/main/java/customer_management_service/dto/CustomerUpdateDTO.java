package customer_management_service.dto;

import customer_management_service.validation.AgeMatchesBirthDate;
import customer_management_service.validation.ValidationConstants;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@AgeMatchesBirthDate
public class CustomerUpdateDTO {
    @Size(min = ValidationConstants.MIN_NAME_LENGTH, max = ValidationConstants.MAX_NAME_LENGTH, message = ValidationConstants.FIRST_NAME_LENGTH)
    @Pattern(regexp = ValidationConstants.NAME_PATTERN, message = ValidationConstants.NAME_PATTERN_MESSAGE)
    @Schema(example = "María")
    private String firstName;
    
    @Size(min = ValidationConstants.MIN_NAME_LENGTH, max = ValidationConstants.MAX_NAME_LENGTH, message = ValidationConstants.LAST_NAME_LENGTH)
    @Pattern(regexp = ValidationConstants.NAME_PATTERN, message = ValidationConstants.NAME_PATTERN_MESSAGE)
    @Schema(example = "García")
    private String lastName;
    
    @Min(value = ValidationConstants.MIN_AGE, message = ValidationConstants.AGE_MIN_MESSAGE)
    @Max(value = ValidationConstants.MAX_AGE, message = ValidationConstants.AGE_MAX_MESSAGE)
    @Schema(example = "25")
    private Integer age;
    
    @Past(message = ValidationConstants.BIRTH_DATE_PAST)
    @Schema(example = "1999-05-15")
    private LocalDate birthDate;
} 