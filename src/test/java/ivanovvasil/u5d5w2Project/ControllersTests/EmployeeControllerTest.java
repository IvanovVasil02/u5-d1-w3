package ivanovvasil.u5d5w2Project;

import com.fasterxml.jackson.databind.ObjectMapper;
import ivanovvasil.u5d5w2Project.controllers.EmployeeController;
import ivanovvasil.u5d5w2Project.entities.Employee;
import ivanovvasil.u5d5w2Project.exceptions.NotFoundException;
import ivanovvasil.u5d5w2Project.payloads.NewEmployeeDTO;
import ivanovvasil.u5d5w2Project.payloads.NewPutEmployeeDTO;
import ivanovvasil.u5d5w2Project.services.EmployeesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
  private static final String ENDPOINT = "/employees";
  Employee employee = Employee.builder().id(2).name("Vasil").surname("Ivanov").email("vsa@vsa.com").build();
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private EmployeesService employeesService;

  @Test
  public void testSaveWrongsEmployeerReturnBadRequest() throws Exception {
    NewEmployeeDTO newEmployeeDTO = new NewEmployeeDTO("", "ivanov", "asd@asdasd.com", "asd");

    String requestBody = objectMapper.writeValueAsString(newEmployeeDTO);
    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSaveEmployeerReturn201() throws Exception {
    NewEmployeeDTO newEmployeeDTO = new NewEmployeeDTO("vasil", "ivanov", "asd@asdasd.com", "asd");

    String requestBody = objectMapper.writeValueAsString(newEmployeeDTO);
    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void testGetAllEmployeerReturnOkk() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  public void testGetEmployeerReturnNotFound() throws Exception {
    int userId = -1;
    Mockito.when(employeesService.findById(userId)).thenThrow(NotFoundException.class);
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + userId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testGetEmployeerReturnOk() throws Exception {
    int employeeId = 2;
    Mockito.when(employeesService.findById(2)).thenReturn(employee);
    String requestBody = objectMapper.writeValueAsString(employee);
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + employeeId)
                    .contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
  }

  @Test
  public void testPutEmployersReturnNotFound() throws Exception {
    int employeeId = 2;
    NewPutEmployeeDTO newPutEmployeeDTO = new NewPutEmployeeDTO("Vasil", "Ivanov", "vas@vas.com", "picture");
    String requestBody = objectMapper.writeValueAsString(newPutEmployeeDTO);

    Mockito.when(employeesService.findByIdAndUpdate(2, newPutEmployeeDTO)).thenThrow(NotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testPutEmployersReturnBadRequest() throws Exception {
    int employeeId = 2;
    NewPutEmployeeDTO newPutEmployeeDTO = new NewPutEmployeeDTO("", "Ivanov", "vas@vas.com", "picture");
    String requestBody = objectMapper.writeValueAsString(newPutEmployeeDTO);

    Mockito.when(employeesService.findByIdAndUpdate(2, newPutEmployeeDTO)).thenThrow(NotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testPutEmployersReturn200() throws Exception {
    int employeeId = 2;
    NewPutEmployeeDTO newPutEmployeeDTO = new NewPutEmployeeDTO("Vasil2", "Ivanovv", "vas@vas.com", "picture");
    String requestBody = objectMapper.writeValueAsString(newPutEmployeeDTO);
    Mockito.when(employeesService.findByIdAndUpdate(2, newPutEmployeeDTO)).thenReturn(employee);
    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testDeleteEmployersReturn404() throws Exception {
    int employeeId = 2;
    Mockito.doThrow(NotFoundException.class).when(employeesService).findByIdAndDelete(2);
    mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT + "/" + employeeId))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testUploadImgReturn200Ok() throws Exception {
    int employeeId = 2;
    String endpoint = ENDPOINT + "/" + employeeId + "/uploadImg";
    MockMultipartFile pictureToUpload = new MockMultipartFile(
            "profileImg", "img1.jpg", "image/jpeg", "img1.jpg".getBytes());
    mockMvc.perform(MockMvcRequestBuilders.multipart(endpoint)
                    .file(pictureToUpload))
            .andExpect(MockMvcResultMatchers.status().isOk());
  }

}
