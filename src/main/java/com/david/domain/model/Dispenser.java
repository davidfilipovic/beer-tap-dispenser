package com.david.domain.model;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by david on 07-03-2023
 */

@Getter
@Setter
public class Dispenser {

  private UUID id;
  private double flowVolume;
  private List<DispenserSpendingLine> dispenserSpendingLines;

}
