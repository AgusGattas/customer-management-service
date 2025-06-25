package customer_management_service.model;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(name = "estimated_event_date")
    private LocalDate estimatedEventDate;
}