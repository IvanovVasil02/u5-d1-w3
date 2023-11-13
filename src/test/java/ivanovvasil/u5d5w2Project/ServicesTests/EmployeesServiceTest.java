package ivanovvasil.u5d5w2Project.ServicesTests;

import com.github.javafaker.Faker;
import ivanovvasil.u5d5w2Project.entities.Employee;
import ivanovvasil.u5d5w2Project.exceptions.BadRequestException;
import ivanovvasil.u5d5w2Project.exceptions.NotFoundException;
import ivanovvasil.u5d5w2Project.payloads.NewEmployeeDTO;
import ivanovvasil.u5d5w2Project.payloads.NewPutEmployeeDTO;
import ivanovvasil.u5d5w2Project.services.EmployeesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmployeesServiceTest {

  Faker faker = new Faker(Locale.ITALY);
  NewEmployeeDTO employeeDTO = new NewEmployeeDTO("Vasil", "Ivanov", faker.internet().emailAddress(), "pic1");
  @Autowired
  private EmployeesService employeesService;

  @Test
  public void TestSaveEmployeeNotNull() throws IOException {
    Employee savedEmploye = employeesService.save(employeeDTO);
    assertNotNull(savedEmploye);
  }

  @Test
  public void TestSaveEmployeeReturnEmailAlreadyIsUsed() throws IOException {
    NewEmployeeDTO employeeDTO1 = new NewEmployeeDTO("Vasil", "Ivanov", "vsasa@gmail.com", "pic1");
    NewEmployeeDTO newemployeeDTO = new NewEmployeeDTO("Vasil2", "Ivanov2", "vsasa@gmail.com", "pic1");
    Assertions.assertThrows(BadRequestException.class, () -> {
      employeesService.save(employeeDTO1);
      employeesService.save(newemployeeDTO);
    });
  }

  @Test
  public void TestFindByIdReturnEmployee() throws IOException {
    Employee savedEmployee = employeesService.save(employeeDTO);
    Employee foundEmployee = employeesService.findById(savedEmployee.getId());
    Assertions.assertEquals(savedEmployee, foundEmployee);
  }

  @Test
  public void TestDeleteReturnNotFoundAfterFindById() throws IOException {
    Employee savedEmployee = employeesService.save(employeeDTO);
    employeesService.findByIdAndDelete(savedEmployee.getId());
    Assertions.assertThrows(NotFoundException.class, () -> {
      Employee foundEmployee = employeesService.findById(savedEmployee.getId());
    });
  }

  @Test
  public void testFindByIdAndUpdateReturnUpdatedEmployee() throws IOException {
    Employee savedEmployee = employeesService.save(employeeDTO);
    NewPutEmployeeDTO updateEmployeeDTO = new NewPutEmployeeDTO("Vasil223", "Ivanov2", faker.internet().emailAddress(), "pic1");
    Employee updatedEmployee = employeesService.findByIdAndUpdate(savedEmployee.getId(), updateEmployeeDTO);
    Assertions.assertNotNull(updatedEmployee);
    Assertions.assertNotEquals(savedEmployee, updatedEmployee);
  }


}
