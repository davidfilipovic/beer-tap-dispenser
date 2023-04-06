package com.david.application.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by david on 07-03-2023
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateDispenserResponse {

  private UUID id;
  private double flowVolume;

}
