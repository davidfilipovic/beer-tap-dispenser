package com.david.infrastructure.persistence.repository;

import com.david.infrastructure.persistence.db.DispenserDb;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by david on 07-03-2023
 */

public interface DispenserJpaRepository extends CrudRepository<DispenserDb, UUID> {
}
