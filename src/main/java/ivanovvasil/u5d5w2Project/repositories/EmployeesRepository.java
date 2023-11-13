package ivanovvasil.u5d5w2Project.repositories;
import ivanovvasil.u5d5w2Project.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeesRepository extends JpaRepository<Employee, Integer> {
  Optional<Employee> findByEmail(String email);
}
