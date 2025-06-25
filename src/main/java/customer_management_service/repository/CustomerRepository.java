package customer_management_service.repository;

import customer_management_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    List<Customer> findAllByOrderByCreationDateDesc();
    
    @Query("SELECT AVG(c.age) FROM Customer c")
    Double getAverageAge();
    
    @Query("SELECT SQRT(AVG(POWER(c.age - (SELECT AVG(c2.age) FROM Customer c2), 2))) FROM Customer c")
    Double getAgeStandardDeviation();
} 