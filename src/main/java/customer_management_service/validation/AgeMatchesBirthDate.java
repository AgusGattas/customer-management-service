package customer_management_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeMatchesBirthDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeMatchesBirthDate {
    String message() default "Age does not match birth date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 