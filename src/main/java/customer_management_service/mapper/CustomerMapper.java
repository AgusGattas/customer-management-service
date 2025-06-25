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
    
    public void updateEntityFromDTO(Customer customer, CustomerUpdateDTO dto) {
        if (dto == null) {
            return;
        }
        
        if (customer == null) {
            throw new InvalidDataException("Customer entity cannot be null for update");
        }
        
        if (dto.getFirstName() != null) {
            if (dto.getFirstName().trim().isEmpty()) {
                throw new InvalidDataException("First name cannot be empty");
            }
            customer.setFirstName(dto.getFirstName().trim());
        }
        if (dto.getLastName() != null) {
            if (dto.getLastName().trim().isEmpty()) {
                throw new InvalidDataException("Last name cannot be empty");
            }
            customer.setLastName(dto.getLastName().trim());
        }
        if (dto.getAge() != null) {
            customer.setAge(dto.getAge());
        }
        if (dto.getBirthDate() != null) {
            customer.setBirthDate(dto.getBirthDate());
        }
    }
} 