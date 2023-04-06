package com.david.domain.services.persistence;

import com.david.domain.model.DispenserSpendingLine;
import java.util.List;
import java.util.UUID;

/**
 * Created by david on 07-03-2023
 */

public interface DispenserSpendingLineRepository {

 DispenserSpendingLine findLatestByDispenserId(UUID id);

 void create(DispenserSpendingLine dispenserSpendingLine);

 void update(DispenserSpendingLine dispenserSpendingLine);

 List<DispenserSpendingLine> findAllByDispenserId(UUID id);
}
