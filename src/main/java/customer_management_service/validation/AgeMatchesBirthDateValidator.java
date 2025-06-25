package customer_management_service.validation;

import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;

public class AgeMatchesBirthDateValidator implements ConstraintValidator<AgeMatchesBirthDate, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Integer age = null;
        LocalDate birthDate = null;

        if (value instanceof CustomerCreateDTO) {
            CustomerCreateDTO dto = (CustomerCreateDTO) value;
            age = dto.getAge();
            birthDate = dto.getBirthDate();
        } else if (value instanceof CustomerUpdateDTO) {
            CustomerUpdateDTO dto = (CustomerUpdateDTO) value;
            age = dto.getAge();
            birthDate = dto.getBirthDate();
        }

        // If both values are not provided, we cannot validate
        if (age == null || birthDate == null) {
            return true;
        }

        // Calculate real age based on birth date
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Period period = Period.between(birthDate, today);
        int calculatedAge = period.getYears();

        // Age must match calculated age (with ±1 year tolerance for edge cases)
        return Math.abs(age - calculatedAge) <= 1;
    }
} 