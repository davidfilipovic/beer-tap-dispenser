package com.david.infrastructure.persistence.implementation;

import com.david.domain.exception.DispenserNotFoundException;
import com.david.domain.exception.DispenserSpendingLineException;
import com.david.domain.model.Dispenser;
import com.david.domain.model.DispenserSpendingLine;
import com.david.domain.model.DispenserStatus;
import com.david.domain.services.persistence.DispenserSpendingLineRepository;
import com.david.infrastructure.persistence.db.DispenserDb;
import com.david.infrastructure.persistence.db.DispenserSpendingLineDb;
import com.david.infrastructure.persistence.repository.DispenserJpaRepository;
import com.david.infrastructure.persistence.repository.DispenserSpendingLineJpaRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by david on 07-03-2023
 */

@Repository
@AllArgsConstructor
public class DispenserSpendingLineRepositoryImpl implements DispenserSpendingLineRepository {

  private final DispenserSpendingLineJpaRepository dispenserSpendingLineJpaRepository;
  private final DispenserJpaRepository dispenserJpaRepository;

  @Override
  public DispenserSpendingLine findLatestByDispenserId(final UUID id) {
    final Optional<DispenserSpendingLineDb> dispenserSpendingLineDb =
        dispenserSpendingLineJpaRepository.findTopByDispenserIdOrderByUpdatedAtDesc(id);

    return dispenserSpendingLineDb.map(this::getDispenserSpendingLine).orElse(null);
  }

  @Override
  public void create(final DispenserSpendingLine dispenserSpendingLine) {
    final DispenserSpendingLineDb dispenserSpendingLineDb = new DispenserSpendingLineDb();

    saveDispenserSpendingLine(dispenserSpendingLine, dispenserSpendingLineDb);
  }

  @Override
  public void update(final DispenserSpendingLine dispenserSpendingLine) {
    final DispenserSpendingLineDb dispenserSpendingLineDb = dispenserSpendingLineJpaRepository
        .findById(dispenserSpendingLine.getId())
        .orElseThrow(() -> new DispenserSpendingLineException("Dispenser spending line not in DB"));

    saveDispenserSpendingLine(dispenserSpendingLine, dispenserSpendingLineDb);
  }

  @Override
  public List<DispenserSpendingLine> findAllByDispenserId(UUID id) {
    List<DispenserSpendingLineDb> dispenserSpendingLineDbList = dispenserSpendingLineJpaRepository.findAllByDispenserIdOrderByUpdatedAtAsc(id);

    return dispenserSpendingLineDbList.stream().map(dslDb -> {
      DispenserSpendingLine dispenserSpendingLine = DispenserSpendingLine.builder().build();
      BeanUtils.copyProperties(dslDb, dispenserSpendingLine);
      return dispenserSpendingLine;
    }).collect(Collectors.toList());
  }

  private void saveDispenserSpendingLine(DispenserSpendingLine dispenserSpendingLine, DispenserSpendingLineDb dispenserSpendingLineDb) {
    DispenserDb dispenserDb = dispenserJpaRepository.findById(dispenserSpendingLine.getDispenser().getId())
        .orElseThrow(() -> new DispenserNotFoundException("Dispenser not in DB"));  // Exception here should never happen

    dispenserSpendingLineDb.setDispenser(dispenserDb);
    dispenserSpendingLineDb.setStatus(dispenserSpendingLine.getStatus().toString());

    switch (dispenserSpendingLine.getStatus()) {
      case OPEN:
        dispenserSpendingLineDb.setOpenedAt(dispenserSpendingLine.getUpdatedAt());
        break;
      case CLOSE:
        dispenserSpendingLineDb.setClosedAt(dispenserSpendingLine.getUpdatedAt());
      default:
        break;
    }

    dispenserSpendingLineDb.setUpdatedAt(ZonedDateTime.now()); // Setting time now for easier sorting
    dispenserSpendingLineJpaRepository.save(dispenserSpendingLineDb);
  }

  private DispenserSpendingLine getDispenserSpendingLine(final DispenserSpendingLineDb dslDb) {
    final DispenserSpendingLine dispenserSpendingLine = DispenserSpendingLine.builder().build();
    BeanUtils.copyProperties(dslDb, dispenserSpendingLine);
    dispenserSpendingLine.setStatus(DispenserStatus.valueOf(dslDb.getStatus()));

    final Dispenser dispenser = new Dispenser();
    BeanUtils.copyProperties(dslDb.getDispenser(), dispenser);
    dispenserSpendingLine.setDispenser(dispenser);

    return dispenserSpendingLine;
  }


}
