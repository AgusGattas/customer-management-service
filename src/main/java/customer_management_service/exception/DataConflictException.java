package customer_management_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataConflictException extends RuntimeException {
    
    public DataConflictException(String message) {
        super(message);
    }
    
    public DataConflictException(String message, Throwable cause) {
        super(message, cause);
    }
} 