package customer_management_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
    
    public CustomerNotFoundException(Long id) {
        super(String.format("Customer not found with ID: %d", id));
    }
} 