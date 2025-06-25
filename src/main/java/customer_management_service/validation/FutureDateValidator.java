package customer_management_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Validator for future dates
 */
public class FutureDateValidator implements ConstraintValidator<FutureDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; 
        }
        
        return value.isAfter(LocalDate.now());
    }
} 