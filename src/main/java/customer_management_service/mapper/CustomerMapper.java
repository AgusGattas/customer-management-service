package customer_management_service.mapper;

import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerDTO;
import customer_management_service.dto.CustomerUpdateDTO;
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
            return null;
        }
        
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setAge(dto.getAge());
        customer.setBirthDate(dto.getBirthDate());
        
        return customer;
    }
    
    public void updateEntityFromDTO(Customer customer, CustomerUpdateDTO dto) {
        if (dto == null) {
            return;
        }
        
        if (dto.getFirstName() != null) {
            customer.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            customer.setLastName(dto.getLastName());
        }
        if (dto.getAge() != null) {
            customer.setAge(dto.getAge());
        }
        if (dto.getBirthDate() != null) {
            customer.setBirthDate(dto.getBirthDate());
        }
    }
} 