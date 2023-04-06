package com.david.application.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by david on 08-03-2023
 */

@Setter
@Getter
public class MoneySpentResponse {

  private BigDecimal amount;
  private List<DispenserSpendingLineResponse> usages;

  @Setter
  @Getter
  public static class DispenserSpendingLineResponse {
    private ZonedDateTime openedAt;
    private ZonedDateTime closedAt;
    private double flowVolume;
    private BigDecimal totalSpent;

  }
}
