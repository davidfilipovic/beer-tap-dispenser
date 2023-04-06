package com.david.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.david.application.DispenserService;
import com.david.application.request.CreateDispenserRequest;
import com.david.application.request.UpdateDispenserStatusRequest;
import com.david.application.response.CreateDispenserResponse;
import com.david.domain.exception.DispenserNotFoundException;
import com.david.domain.exception.DispenserStatusInConflictException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Created by david on 08-03-2023
 */

@SpringBootTest
@AutoConfigureMockMvc
public class DispenserControllerTest {

  private static final UUID ID = UUID.fromString("ca69a309-4fe4-4a08-8226-679ddd853c8b");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private DispenserService dispenserService;

  private UpdateDispenserStatusRequest updateDispenserStatusRequest;

  @BeforeEach
  void setUp() {
    updateDispenserStatusRequest = new UpdateDispenserStatusRequest();
    updateDispenserStatusRequest.setStatus("open");
  }

  @Test
  public void testCreateDispenser() throws Exception {
    final double flowVolume = 1.0;

    CreateDispenserRequest request = new CreateDispenserRequest();
    request.setFlowVolume(flowVolume);

    CreateDispenserResponse response = new CreateDispenserResponse();
    response.setId(ID);
    response.setFlowVolume(flowVolume);

    // mock the dispenser service to return the test response body
    when(dispenserService.createDispenser(any(CreateDispenserRequest.class))).thenReturn(response);

    // Perform a POST request to the endpoint and verify the response
    mockMvc.perform(post("/dispenser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(ID.toString()))
        .andExpect(jsonPath("$.flowVolume").value(flowVolume));
  }

  @Test
  void testUpdateDispenserStatus() throws Exception {
    doNothing().when(dispenserService).updateDispenserStatus(any(UpdateDispenserStatusRequest.class), any(UUID.class));

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/dispenser/{id}/status", ID)
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(updateDispenserStatusRequest)));

    resultActions.andExpect(status().isAccepted());
  }

  @Test
  void testUpdateDispenserStatusDispenserNotFound() throws Exception {
    String errorMessage = "Dispenser with the Id " + ID + " is not found";
    doThrow(new DispenserNotFoundException(errorMessage)).when(dispenserService).updateDispenserStatus(any(UpdateDispenserStatusRequest.class),
                                                                                                       any(UUID.class));

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/dispenser/{id}/status", ID)
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(updateDispenserStatusRequest)));

    resultActions.andExpect(status().isNotFound()).andExpect(content().string(errorMessage));
  }

  @Test
  void testUpdateDispenserStatusStatusInConflict() throws Exception {
    String errorMessage = "Dispenser is in conflicting status. It is is already closed.";
    doThrow(new DispenserStatusInConflictException(errorMessage)).when(dispenserService).updateDispenserStatus(
        any(UpdateDispenserStatusRequest.class), any(UUID.class));

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/dispenser/{id}/status", ID)
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(updateDispenserStatusRequest)));

    resultActions.andExpect(status().isConflict()).andExpect(content().string(errorMessage));
  }


}
