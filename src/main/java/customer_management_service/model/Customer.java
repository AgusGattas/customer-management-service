package customer_management_service.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
@Schema(description = "System customer")
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseModel {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Min(0)
    @Max(150)
    @Column(nullable = false)
    private Integer age;

    @NotNull
    @Past
    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(name = "estimated_event_date")
    private LocalDate estimatedEventDate;
}