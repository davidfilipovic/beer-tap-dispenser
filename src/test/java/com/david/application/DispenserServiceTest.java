package com.david.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.david.application.DispenserService;
import com.david.application.DispenserTransformer;
import com.david.application.request.CreateDispenserRequest;
import com.david.application.response.CreateDispenserResponse;
import com.david.domain.model.Dispenser;
import com.david.domain.services.persistence.DispenserRepository;
import com.david.domain.services.persistence.DispenserSpendingLineRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by david on 09-03-2023
 */

@SpringBootTest
public class DispenserServiceTest {

  private static final UUID ID = UUID.fromString("ca69a309-4fe4-4a08-8226-679ddd853c8b");

  private DispenserService dispenserService;

  @Mock
  private DispenserRepository dispenserRepository;

  @Mock
  private DispenserSpendingLineRepository dispenserSpendingLineRepository;

  @Mock
  private DispenserTransformer dispenserTransformer;

  @BeforeEach
  void setUp() {
    dispenserService = new DispenserService(dispenserRepository, dispenserSpendingLineRepository, dispenserTransformer);
  }

  @Test
  void testCreateDispenser() {
    CreateDispenserRequest createDispenserRequest = new CreateDispenserRequest();
    createDispenserRequest.setFlowVolume(1.0);

    when(dispenserRepository.save(any(Dispenser.class))).thenReturn(ID);

    CreateDispenserResponse createDispenserResponse = dispenserService.createDispenser(createDispenserRequest);

    verify(dispenserRepository).save(any(Dispenser.class));
    assertEquals(createDispenserResponse.getFlowVolume(), 1.0);
    assertEquals(createDispenserResponse.getId(), ID);
  }


}
