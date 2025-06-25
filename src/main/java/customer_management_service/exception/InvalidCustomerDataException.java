package customer_management_service.exception;

/**
 * Excepción lanzada cuando los datos del cliente son inválidos.
 */
public class InvalidCustomerDataException extends RuntimeException {
    
    public InvalidCustomerDataException(String message) {
        super(message);
    }
    
    public InvalidCustomerDataException(String message, Throwable cause) {
        super(message, cause);
    }
} 