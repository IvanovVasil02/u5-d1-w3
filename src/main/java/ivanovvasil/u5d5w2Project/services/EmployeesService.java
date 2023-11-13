package ivanovvasil.u5d5w2Project.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import ivanovvasil.u5d5w2Project.entities.Device;
import ivanovvasil.u5d5w2Project.entities.Employee;
import ivanovvasil.u5d5w2Project.enums.DeviceStatus;
import ivanovvasil.u5d5w2Project.exceptions.BadRequestException;
import ivanovvasil.u5d5w2Project.exceptions.NotFoundException;
import ivanovvasil.u5d5w2Project.payloads.NewEmployeeDTO;
import ivanovvasil.u5d5w2Project.payloads.NewPutEmployeeDTO;
import ivanovvasil.u5d5w2Project.repositories.DevicesRepository;
import ivanovvasil.u5d5w2Project.repositories.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class EmployeesService {
  @Autowired
  private EmployeesRepository employeesRepository;
  @Autowired
  private DevicesRepository devicesRepository;
  @Autowired
  private Cloudinary cloudinary;

  //findALl for employees runner
  public Employee saveRunnerEmployee(Employee employee) {
    return employeesRepository.save(employee);
  }

  public Employee save(NewEmployeeDTO body) throws IOException {
    employeesRepository.findByEmail(body.email()).ifPresent(author -> {
      throw new BadRequestException("The email  " + author.getEmail() + " is already used!");
    });
    Employee newEmployee = new Employee();
    newEmployee.setName(body.name());
    newEmployee.setSurname(body.surname());
    newEmployee.setEmail(body.email());
    if (body.profilePicture() != null) {
      newEmployee.setProfilePicture(body.profilePicture());
    } else {
      newEmployee.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTC0HlQ_ckX6HqCAlqroocyRDx_ZRu3x3ezoA&usqp=CAU");
    }
    return employeesRepository.save(newEmployee);
  }

  //findALl for employees runner
  public List<Employee> findAll() {
    return employeesRepository.findAll();
  }

  public Page<Employee> findAll(int page, int size, String orderBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
    return employeesRepository.findAll(pageable);
  }

  public Employee findById(int id) throws NotFoundException {
    return employeesRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
  }

  public void findByIdAndDelete(int id) throws NotFoundException {
    List<Device> devicesList = devicesRepository.findAllByEmployeeId(id);
    devicesList.forEach(device -> {
      device.setDeviceStatus(DeviceStatus.AVAILABLE);
      device.setEmployee(null);
      devicesRepository.save(device);
    });
    employeesRepository.delete(this.findById(id));
  }

  public Employee findByIdAndUpdate(int id, NewPutEmployeeDTO body) throws IOException {
    Employee found = this.findById(id);
    found.setName(body.name());
    found.setSurname(body.surname());
    found.setEmail(body.email());
    return employeesRepository.save(found);
  }

  public Employee uploadImg(int id, MultipartFile file) throws IOException {
    Employee found = this.findById(id);
    String urlImg = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
    found.setProfilePicture(urlImg);
    employeesRepository.save(found);
    return found;
  }
}
