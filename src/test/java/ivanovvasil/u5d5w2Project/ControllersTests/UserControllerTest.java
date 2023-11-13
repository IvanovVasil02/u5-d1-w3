package ivanovvasil.u5d5w2Project;

import com.fasterxml.jackson.databind.ObjectMapper;
import ivanovvasil.u5d5w2Project.controllers.UserController;
import ivanovvasil.u5d5w2Project.entities.User;
import ivanovvasil.u5d5w2Project.exceptions.NotFoundException;
import ivanovvasil.u5d5w2Project.payloads.NewPutUserDTO;
import ivanovvasil.u5d5w2Project.payloads.NewUserDTO;
import ivanovvasil.u5d5w2Project.services.UsersService;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {
  private static final String ENDPOINT = "/employees";
  User user = User.builder().id(2).name("Vasil").surname("Ivanov").email("vsa@vsa.com").build();
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private UsersService usersService;

  @Test
  public void testSaveWrongsEmployeerReturnBadRequest() throws Exception {
    NewUserDTO newUserDTO = new NewUserDTO("", "ivanov", "asd@asdasd.com", "asd");

    String requestBody = objectMapper.writeValueAsString(newUserDTO);
    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSaveEmployeerReturn201() throws Exception {
    NewUserDTO newUserDTO = new NewUserDTO("vasil", "ivanov", "asd@asdasd.com", "asd");

    String requestBody = objectMapper.writeValueAsString(newUserDTO);
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
    Mockito.when(usersService.findById(userId)).thenThrow(NotFoundException.class);
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + userId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testGetEmployeerReturnOk() throws Exception {
    int employeeId = 2;
    Mockito.when(usersService.findById(2)).thenReturn(employee);
    String requestBody = objectMapper.writeValueAsString(employee);
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + employeeId)
                    .contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
  }

  @Test
  public void testPutEmployersReturnNotFound() throws Exception {
    int employeeId = 2;
    NewPutUserDTO newPutUserDTO = new NewPutUserDTO("Vasil", "Ivanov", "vas@vas.com", "picture");
    String requestBody = objectMapper.writeValueAsString(newPutUserDTO);

    Mockito.when(usersService.findByIdAndUpdate(2, newPutUserDTO)).thenThrow(NotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testPutEmployersReturnBadRequest() throws Exception {
    int employeeId = 2;
    NewPutUserDTO newPutUserDTO = new NewPutUserDTO("", "Ivanov", "vas@vas.com", "picture");
    String requestBody = objectMapper.writeValueAsString(newPutUserDTO);

    Mockito.when(usersService.findByIdAndUpdate(2, newPutUserDTO)).thenThrow(NotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testPutEmployersReturn200() throws Exception {
    int employeeId = 2;
    NewPutUserDTO newPutUserDTO = new NewPutUserDTO("Vasil2", "Ivanovv", "vas@vas.com", "picture");
    String requestBody = objectMapper.writeValueAsString(newPutUserDTO);
    Mockito.when(usersService.findByIdAndUpdate(2, newPutUserDTO)).thenReturn(employee);
    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testDeleteEmployersReturn404() throws Exception {
    int employeeId = 2;
    Mockito.doThrow(NotFoundException.class).when(usersService).findByIdAndDelete(2);
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
