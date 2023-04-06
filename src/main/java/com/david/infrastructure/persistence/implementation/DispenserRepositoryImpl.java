package com.david.infrastructure.persistence.implementation;

import com.david.domain.model.Dispenser;
import com.david.domain.services.persistence.DispenserRepository;
import com.david.infrastructure.persistence.db.DispenserDb;
import com.david.infrastructure.persistence.repository.DispenserJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by david on 07-03-2023
 */

@Repository
@AllArgsConstructor
public class DispenserRepositoryImpl implements DispenserRepository {

  private final DispenserJpaRepository dispenserJpaRepository;

  @Override
  public UUID save(Dispenser dispenser) {
    final DispenserDb dispenserDb = new DispenserDb(dispenser.getFlowVolume());

    dispenserJpaRepository.save(dispenserDb);

    return dispenserDb.getId();
  }

  @Override
  public Optional<Dispenser> findById(UUID id) {
    Optional<DispenserDb> dispenserDbOptional = dispenserJpaRepository.findById(id);

    return dispenserDbOptional.map(dispenserDb -> {
      Dispenser dispenser = new Dispenser();
      BeanUtils.copyProperties(dispenserDb, dispenser);
      return dispenser;
    });
  }
}
