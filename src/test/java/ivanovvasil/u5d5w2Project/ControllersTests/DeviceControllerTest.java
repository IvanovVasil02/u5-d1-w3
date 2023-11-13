package ivanovvasil.u5d5w2Project;

import com.fasterxml.jackson.databind.ObjectMapper;
import ivanovvasil.u5d5w2Project.controllers.DeviceController;
import ivanovvasil.u5d5w2Project.entities.Device;
import ivanovvasil.u5d5w2Project.exceptions.NotFoundException;
import ivanovvasil.u5d5w2Project.payloads.NewDeviceDTO;
import ivanovvasil.u5d5w2Project.payloads.NewPutDeviceDTO;
import ivanovvasil.u5d5w2Project.services.DevicesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {
  private static final String ENDPOINT = "/devices";
  Device device = Device.builder().build();
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private DevicesService devicesService;

  @Test
  public void testSaveWrongsDeviceReturnBadRequest() throws Exception {
    NewDeviceDTO newDeviceDTO = new NewDeviceDTO("LAPTP", "model1");

    String requestBody = objectMapper.writeValueAsString(newDeviceDTO);
    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSaveDeviceReturn201() throws Exception {
    NewDeviceDTO newDeviceDTO = new NewDeviceDTO("LAPTOP", "Model2");

    String requestBody = objectMapper.writeValueAsString(newDeviceDTO);
    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void testGetAllDeviceReturnOkk() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  public void testGetDeviceReturnNotFound() throws Exception {
    int userId = -1;
    Mockito.when(devicesService.findById(userId)).thenThrow(NotFoundException.class);
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + userId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testGetDeviceReturnOk() throws Exception {
    int deviceId = 2;
    Mockito.when(devicesService.findById(2)).thenReturn(device);
    String requestBody = objectMapper.writeValueAsString(device);
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + deviceId)
                    .contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
  }

  @Test
  public void testPutEmployersReturnNotFound() throws Exception {
    int deviceId = 2;
    NewPutDeviceDTO newPutDeviceDTO = new NewPutDeviceDTO("LAPTOP", "Model3", "AVAILABLE", 4);
    String requestBody = objectMapper.writeValueAsString(newPutDeviceDTO);

    Mockito.when(devicesService.findByIdAndUpdate(2, newPutDeviceDTO)).thenThrow(NotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + deviceId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testPutEmployersReturnBadRequest() throws Exception {
    int deviceId = 2;
    NewPutDeviceDTO newPutDeviceDTO = new NewPutDeviceDTO("", "", "", 3);
    String requestBody = objectMapper.writeValueAsString(newPutDeviceDTO);

    Mockito.when(devicesService.findByIdAndUpdate(2, newPutDeviceDTO)).thenThrow(NotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + deviceId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testPutEmployersReturn200() throws Exception {
    int deviceId = 2;
    NewPutDeviceDTO newPutDeviceDTO = new NewPutDeviceDTO("LAPTOP", "Model3", "AVAILABLE", 4);
    String requestBody = objectMapper.writeValueAsString(newPutDeviceDTO);
    Mockito.when(devicesService.findByIdAndUpdate(2, newPutDeviceDTO)).thenReturn(device);
    mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" + deviceId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testDeleteEmployersReturn404() throws Exception {
    int deviceId = 2;
    Mockito.doThrow(NotFoundException.class).when(devicesService).findByIdAndDelete(2);
    mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT + "/" + deviceId))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

}
