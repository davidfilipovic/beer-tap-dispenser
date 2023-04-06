package com.david.application.request;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by david on 07-03-2023
 */

@Getter
@Setter
public class UpdateDispenserStatusRequest {

  private String status;
  private ZonedDateTime updatedAt;

}
