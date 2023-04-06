package com.david.domain.services.persistence;

import com.david.domain.model.Dispenser;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by david on 07-03-2023
 */

public interface DispenserRepository {

  UUID save(Dispenser dispenser);

  Optional<Dispenser> findById(UUID id);

}
