package ivanovvasil.u5d5w2Project.Runners;

import ivanovvasil.u5d5w2Project.entities.Employee;
import ivanovvasil.u5d5w2Project.services.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class EmployeesRunner implements CommandLineRunner {
  @Autowired
  private EmployeesService employeesService;

  @Override
  public void run(String... args) throws Exception {
    for (int i = 0; i < 30; i++) {
      Employee newEmployee = Employee.builder().build();
      employeesService.saveRunnerEmployee(newEmployee);
    }
  }
}
