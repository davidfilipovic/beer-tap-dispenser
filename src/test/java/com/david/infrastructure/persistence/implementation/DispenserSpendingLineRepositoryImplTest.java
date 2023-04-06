package com.david.infrastructure.persistence.implementation;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.david.domain.model.Dispenser;
import com.david.domain.model.DispenserSpendingLine;
import com.david.domain.model.DispenserStatus;
import com.david.infrastructure.persistence.db.DispenserDb;
import com.david.infrastructure.persistence.db.DispenserSpendingLineDb;
import com.david.infrastructure.persistence.repository.DispenserJpaRepository;
import com.david.infrastructure.persistence.repository.DispenserSpendingLineJpaRepository;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by david on 08-03-2023
 */

@SpringBootTest
public class DispenserSpendingLineRepositoryImplTest {

  @Mock
  DispenserJpaRepository dispenserJpaRepository;

  @Mock
  private DispenserSpendingLineJpaRepository dispenserSpendingLineJpaRepository;

  private DispenserSpendingLineRepositoryImpl dispenserSpendingLineRepositoryImpl;

  private final static ZonedDateTime OPENED_AT = ZonedDateTime.now();
  private final static ZonedDateTime UPDATED_AT = ZonedDateTime.now();

  @BeforeEach
  void setup() throws Exception {
    MockitoAnnotations.initMocks(this);

    dispenserSpendingLineRepositoryImpl = new DispenserSpendingLineRepositoryImpl(dispenserSpendingLineJpaRepository, dispenserJpaRepository);
  }

  @Test
  void testCreate() {
    Dispenser dispenser = new Dispenser();
    dispenser.setId(UUID.randomUUID());
    dispenser.setFlowVolume(1.0);

    DispenserDb dispenserDb = new DispenserDb(dispenser.getFlowVolume());
    dispenserDb.setId(dispenser.getId());

    DispenserSpendingLine dispenserSpendingLine = getDispenserSpendingLine(dispenser);

    when(dispenserJpaRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenserDb));

    DispenserSpendingLineDb expectedDispenserSpendingLineDb = getDispenserSpendingLineDb(dispenserDb, dispenserSpendingLine);

    dispenserSpendingLineRepositoryImpl.create(dispenserSpendingLine);

    verify(dispenserJpaRepository).findById(dispenser.getId());
  }

  private static DispenserSpendingLineDb getDispenserSpendingLineDb(DispenserDb dispenserDb, DispenserSpendingLine dispenserSpendingLine) {
    DispenserSpendingLineDb expectedDispenserSpendingLineDb = new DispenserSpendingLineDb();

    expectedDispenserSpendingLineDb.setDispenser(dispenserDb);
    expectedDispenserSpendingLineDb.setStatus(dispenserSpendingLine.getStatus().toString());
    expectedDispenserSpendingLineDb.setOpenedAt(dispenserSpendingLine.getUpdatedAt());
    expectedDispenserSpendingLineDb.setUpdatedAt(UPDATED_AT);

    return expectedDispenserSpendingLineDb;
  }

  private static DispenserSpendingLine getDispenserSpendingLine(Dispenser dispenser) {
    DispenserSpendingLine dispenserSpendingLine = DispenserSpendingLine.builder().build();

    dispenserSpendingLine.setId(UUID.randomUUID());
    dispenserSpendingLine.setDispenser(dispenser);
    dispenserSpendingLine.setStatus(DispenserStatus.OPEN);
    dispenserSpendingLine.setOpenedAt(OPENED_AT);
    dispenserSpendingLine.setUpdatedAt(UPDATED_AT);

    return dispenserSpendingLine;
  }
}


