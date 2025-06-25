package customer_management_service.exception;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    @Schema(example = "{\"firstName\": \"The first name can only contain letters and spaces\", \"age\": \"Age must be less than or equal to 150\"}", 
            description = "Map of field names to validation error messages")
    private Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, LocalDateTime timestamp, Map<String, String> errors) {
        super(status, message, timestamp);
        this.errors = errors;
    }
} 