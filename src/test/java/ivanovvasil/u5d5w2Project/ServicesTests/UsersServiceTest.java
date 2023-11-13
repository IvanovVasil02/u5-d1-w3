package ivanovvasil.u5d5w2Project.ServicesTests;

import com.github.javafaker.Faker;
import ivanovvasil.u5d5w2Project.entities.User;
import ivanovvasil.u5d5w2Project.exceptions.BadRequestException;
import ivanovvasil.u5d5w2Project.exceptions.NotFoundException;
import ivanovvasil.u5d5w2Project.payloads.NewPutUserDTO;
import ivanovvasil.u5d5w2Project.payloads.NewUserDTO;
import ivanovvasil.u5d5w2Project.services.UsersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UsersServiceTest {

  Faker faker = new Faker(Locale.ITALY);
  NewUserDTO employeeDTO = new NewUserDTO("Vasil", "Ivanov", faker.internet().emailAddress(), "pic1");
  @Autowired
  private UsersService usersService;

  @Test
  public void TestSaveEmployeeNotNull() throws IOException {
    User savedEmploye = usersService.save(employeeDTO);
    assertNotNull(savedEmploye);
  }

  @Test
  public void TestSaveEmployeeReturnEmailAlreadyIsUsed() throws IOException {
    NewUserDTO employeeDTO1 = new NewUserDTO("Vasil", "Ivanov", "vsasa@gmail.com", "pic1");
    NewUserDTO newemployeeDTO = new NewUserDTO("Vasil2", "Ivanov2", "vsasa@gmail.com", "pic1");
    Assertions.assertThrows(BadRequestException.class, () -> {
      usersService.save(employeeDTO1);
      usersService.save(newemployeeDTO);
    });
  }

  @Test
  public void TestFindByIdReturnEmployee() throws IOException {
    User savedUser = usersService.save(employeeDTO);
    User foundUser = usersService.findById(savedUser.getId());
    Assertions.assertEquals(savedUser, foundUser);
  }

  @Test
  public void TestDeleteReturnNotFoundAfterFindById() throws IOException {
    User savedUser = usersService.save(employeeDTO);
    usersService.findByIdAndDelete(savedUser.getId());
    Assertions.assertThrows(NotFoundException.class, () -> {
      User foundUser = usersService.findById(savedUser.getId());
    });
  }

  @Test
  public void testFindByIdAndUpdateReturnUpdatedEmployee() throws IOException {
    User savedUser = usersService.save(employeeDTO);
    NewPutUserDTO updateEmployeeDTO = new NewPutUserDTO("Vasil223", "Ivanov2", faker.internet().emailAddress(), "pic1");
    User updatedUser = usersService.findByIdAndUpdate(savedUser.getId(), updateEmployeeDTO);
    Assertions.assertNotNull(updatedUser);
    Assertions.assertNotEquals(savedUser, updatedUser);
  }


}
