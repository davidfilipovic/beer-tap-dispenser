package com.david.application;

import com.david.application.response.MoneySpentResponse;
import com.david.domain.model.Dispenser;
import com.david.domain.model.DispenserSpendingLine;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Created by david on 07-03-2023
 */

@Component
public class DispenserTransformer {

  private static final BigDecimal PRICE_PER_LITRE = new BigDecimal("12.25");

  public MoneySpentResponse transformToMoneySpentResponse(final Dispenser dispenser, final List<DispenserSpendingLine> dispenserSpendingLine) {
    final MoneySpentResponse moneySpentResponse = new MoneySpentResponse();

    final List<MoneySpentResponse.DispenserSpendingLineResponse> usages = dispenserSpendingLine.stream().map(dsl -> {
      final MoneySpentResponse.DispenserSpendingLineResponse usage = new MoneySpentResponse.DispenserSpendingLineResponse();

      if (dsl.getClosedAt() == null) {
        dsl.setClosedAt(ZonedDateTime.now());
      }

      final Duration tapOpenDuration = Duration.between(dsl.getOpenedAt(), dsl.getClosedAt());

      final long seconds = tapOpenDuration.getSeconds();
      final BigDecimal totalLitresSpent = BigDecimal.valueOf(dispenser.getFlowVolume() * seconds);
      final BigDecimal totalSpent = calculateTotalSpent(totalLitresSpent);

      usage.setTotalSpent(totalSpent);
      usage.setOpenedAt(dsl.getOpenedAt());
      usage.setClosedAt(dsl.getClosedAt());

      usage.setFlowVolume(dispenser.getFlowVolume());

      return usage;
    }).collect(Collectors.toList());

    moneySpentResponse.setUsages(usages);
    moneySpentResponse.setAmount(usages.stream()
                                     .map(MoneySpentResponse.DispenserSpendingLineResponse::getTotalSpent)
                                     .reduce(BigDecimal.ZERO, BigDecimal::add));

    return moneySpentResponse;
  }

  private BigDecimal calculateTotalSpent(BigDecimal totalLitresSpent) {
    return PRICE_PER_LITRE.multiply(totalLitresSpent);
  }
}
