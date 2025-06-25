package customer_management_service.validation;

/**
 * Constants for validation
 */
public final class ValidationConstants {
    
    private ValidationConstants() {
        // Private constructor to prevent instantiation
    }
    
    // Constants for names
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    public static final String NAME_PATTERN = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";
    
    // Constants for age
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 150;
    
    // Error messages
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String AGE_REQUIRED = "Age is required";
    public static final String BIRTH_DATE_REQUIRED = "Birth date is required";
    
    public static final String FIRST_NAME_LENGTH = "The first name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters long";
    public static final String LAST_NAME_LENGTH = "The last name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters long";
    public static final String NAME_PATTERN_MESSAGE = "The name can only contain letters and spaces";
    
    public static final String AGE_MIN_MESSAGE = "Age must be greater than or equal to " + MIN_AGE;
    public static final String AGE_MAX_MESSAGE = "Age must be less than or equal to " + MAX_AGE;
    public static final String BIRTH_DATE_PAST = "Birth date must be in the past";
} 