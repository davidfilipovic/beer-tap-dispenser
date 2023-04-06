package com.david.infrastructure.persistence.repository;

import com.david.infrastructure.persistence.db.DispenserSpendingLineDb;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by david on 07-03-2023
 */

public interface DispenserSpendingLineJpaRepository extends CrudRepository<DispenserSpendingLineDb, UUID> {
  Optional<DispenserSpendingLineDb> findTopByDispenserIdOrderByUpdatedAtDesc(UUID dispenserId);

  List<DispenserSpendingLineDb> findAllByDispenserIdOrderByUpdatedAtAsc(UUID id);
}
