package com.david.domain.model;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by david on 07-03-2023
 */

@Getter
@Setter
@Builder
@ToString
public class DispenserSpendingLine {

  private UUID id;
  private DispenserStatus status;
  private ZonedDateTime openedAt;
  private ZonedDateTime closedAt;
  private ZonedDateTime updatedAt;
  private Dispenser dispenser;

}
