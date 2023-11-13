package ivanovvasil.u5d5w2Project.ServicesTests;

import com.github.javafaker.Faker;
import ivanovvasil.u5d5w2Project.entities.Device;
import ivanovvasil.u5d5w2Project.exceptions.NotFoundException;
import ivanovvasil.u5d5w2Project.payloads.NewDeviceDTO;
import ivanovvasil.u5d5w2Project.payloads.NewPutDeviceDTO;
import ivanovvasil.u5d5w2Project.services.DevicesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DevicesServiceTest {

  Faker faker = new Faker(Locale.ITALY);
  NewDeviceDTO deviceDTO = new NewDeviceDTO("LAPTOP", "Model2");
  @Autowired
  private DevicesService employeesService;

  @Test
  public void TestSaveDeviceNotNull() throws IOException {
    Device savedEmploye = employeesService.save(deviceDTO);
    assertNotNull(savedEmploye);
  }

  @Test
  public void TestFindByIdReturnDevice() throws IOException {
    NewDeviceDTO deviceDTO = new NewDeviceDTO("LAPTOP", "Model2");
    Device savedDevice = employeesService.save(deviceDTO);
    Device foundDevice = employeesService.findById(savedDevice.getId());
    Assertions.assertEquals(savedDevice, foundDevice);
  }

  @Test
  public void TestDeleteReturnNotFoundAfterFindById() throws IOException {
    Device savedDevice = employeesService.save(deviceDTO);
    employeesService.findByIdAndDelete(savedDevice.getId());
    Assertions.assertThrows(NotFoundException.class, () -> {
      Device foundDevice = employeesService.findById(savedDevice.getId());
    });
  }

  @Test
  public void testFindByIdAndUpdateReturnUpdatedDevice() throws IOException {

    Device savedDevice = employeesService.save(deviceDTO);
    NewPutDeviceDTO updateDeviceDTO = new NewPutDeviceDTO("LAPTOP", "Model3", "AVAILABLE", null);
    Device updatedDevice = employeesService.findByIdAndUpdate(savedDevice.getId(), updateDeviceDTO);
    System.out.println(savedDevice);
    System.out.println(updatedDevice);

    Assertions.assertNotEquals(savedDevice, updatedDevice);
  }
}
