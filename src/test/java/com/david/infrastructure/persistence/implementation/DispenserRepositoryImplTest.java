package com.david.infrastructure.persistence.implementation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.david.domain.model.Dispenser;
import com.david.infrastructure.persistence.db.DispenserDb;
import com.david.infrastructure.persistence.repository.DispenserJpaRepository;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by david on 08-03-2023
 */

@SpringBootTest
@AutoConfigureTestDatabase
public class DispenserRepositoryImplTest {

  private static final UUID ID = UUID.fromString("ca69a309-4fe4-4a08-8226-679ddd853c8b");

  @Mock
  private DispenserJpaRepository dispenserJpaRepository;

  private DispenserRepositoryImpl dispenserRepositoryImpl;

  @BeforeEach
  void setup() throws Exception {
    MockitoAnnotations.initMocks(this);

    dispenserRepositoryImpl = new DispenserRepositoryImpl(dispenserJpaRepository);
  }

  @Test
  void testSave() {
    Dispenser dispenser = new Dispenser();
    dispenser.setFlowVolume(1.0);

    DispenserDb savedDispenserDb = new DispenserDb();
    savedDispenserDb.setId(ID);
    savedDispenserDb.setFlowVolume(1.0);
    savedDispenserDb.setDispenserSpendingLineDbs(new ArrayList<>());

    when(dispenserJpaRepository.save(any(DispenserDb.class))).thenReturn(savedDispenserDb);

    dispenserRepositoryImpl.save(dispenser);

    verify(dispenserJpaRepository).save(any(DispenserDb.class));
  }

  @Test
  void testFindById() {
    DispenserDb dispenserDb = new DispenserDb();
    dispenserDb.setId(ID);
    dispenserDb.setFlowVolume(1.0);
    dispenserDb.setDispenserSpendingLineDbs(new ArrayList<>());

    when(dispenserJpaRepository.findById(ID)).thenReturn(java.util.Optional.of(dispenserDb));

    dispenserRepositoryImpl.findById(ID);

    verify(dispenserJpaRepository).findById(ID);
  }

}
