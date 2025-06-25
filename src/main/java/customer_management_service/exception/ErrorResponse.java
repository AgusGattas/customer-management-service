package customer_management_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @Schema(example = "404", description = "HTTP status code")
    private int status;
    
    @Schema(example = "No se encontró el cliente con ID: 1", description = "Error message")
    private String message;
    
    @Schema(example = "2024-01-15T10:30:00", description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;
} 