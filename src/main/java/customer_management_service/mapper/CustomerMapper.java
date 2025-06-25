package customer_management_service.mapper;

import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import customer_management_service.exception.InvalidDataException;
import customer_management_service.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setAge(customer.getAge());
        dto.setBirthDate(customer.getBirthDate());
        dto.setEstimatedEventDate(customer.getEstimatedEventDate());
        dto.setCreationDate(customer.getCreationDate());
        dto.setUpdateDate(customer.getUpdateDate());
        
        return dto;
    }
    
    public Customer toEntity(CustomerCreateDTO dto) {
        if (dto == null) {
            throw new InvalidDataException("Customer data cannot be null");
        }
        
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new InvalidDataException("First name cannot be null or empty");
        }
        
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new InvalidDataException("Last name cannot be null or empty");
        }
        
        if (dto.getAge() == null) {
            throw new InvalidDataException("Age cannot be null");
        }
        
        if (dto.getBirthDate() == null) {
            throw new InvalidDataException("Birth date cannot be null");
        }
        
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName().trim());
        customer.setLastName(dto.getLastName().trim());
        customer.setAge(dto.getAge());
        customer.setBirthDate(dto.getBirthDate());
        
        return customer;
    }
    
    /**
     * Updates a Customer entity with data from CustomerUpdateDTO.
     * Only updates non-null fields and validates string fields.
     * 
     * @param customer the customer entity to update
     * @param dto the DTO containing update data
     * @throws InvalidDataException if customer is null or string fields are empty
     */
    public void updateEntityFromDTO(Customer customer, CustomerUpdateDTO dto) {
        validateInputs(customer, dto);
        
        updateStringFieldIfPresent(customer::setFirstName, dto.getFirstName(), "First name");
        updateStringFieldIfPresent(customer::setLastName, dto.getLastName(), "Last name");
        updateFieldIfPresent(customer::setAge, dto.getAge());
        updateFieldIfPresent(customer::setBirthDate, dto.getBirthDate());
    }

    /**
     * Validates that customer and dto are not null.
     * 
     * @param customer the customer entity
     * @param dto the update DTO
     * @throws InvalidDataException if customer is null
     */
    private void validateInputs(Customer customer, CustomerUpdateDTO dto) {
        if (dto == null) {
            return;
        }
        
        if (customer == null) {
            throw new InvalidDataException("Customer entity cannot be null for update");
        }
    }

    /**
     * Updates a string field if the value is present and not empty.
     * 
     * @param setter the setter method for the field
     * @param value the value to set
     * @param fieldName the name of the field for error messages
     * @throws InvalidDataException if the value is empty after trimming
     */
    private void updateStringFieldIfPresent(java.util.function.Consumer<String> setter, String value, String fieldName) {
        if (value != null) {
            String trimmedValue = value.trim();
            if (trimmedValue.isEmpty()) {
                throw new InvalidDataException(fieldName + " cannot be empty");
            }
            setter.accept(trimmedValue);
        }
    }

    /**
     * Updates a field if the value is present.
     * 
     * @param setter the setter method for the field
     * @param value the value to set
     * @param <T> the type of the field
     */
    private <T> void updateFieldIfPresent(java.util.function.Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
} 