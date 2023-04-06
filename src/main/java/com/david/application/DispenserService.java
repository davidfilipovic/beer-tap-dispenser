package com.david.application;

import static com.david.domain.model.DispenserStatus.CLOSE;
import static com.david.domain.model.DispenserStatus.OPEN;

import com.david.application.request.CreateDispenserRequest;
import com.david.application.request.UpdateDispenserStatusRequest;
import com.david.application.response.CreateDispenserResponse;
import com.david.application.response.MoneySpentResponse;
import com.david.domain.exception.DispenserNotFoundException;
import com.david.domain.exception.DispenserStatusInConflictException;
import com.david.domain.model.Dispenser;
import com.david.domain.model.DispenserSpendingLine;
import com.david.domain.model.DispenserStatus;
import com.david.domain.services.persistence.DispenserRepository;
import com.david.domain.services.persistence.DispenserSpendingLineRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by david on 07-03-2023
 */

@Service
@AllArgsConstructor
public class DispenserService {

  private final DispenserRepository dispenserRepository;
  private final DispenserSpendingLineRepository dispenserSpendingLineRepository;
  private final DispenserTransformer dispenserTransformer;

  public CreateDispenserResponse createDispenser(final CreateDispenserRequest createDispenserRequest) {
    final double flowVolume = createDispenserRequest.getFlowVolume();

    final Dispenser dispenser = new Dispenser();
    dispenser.setFlowVolume(flowVolume);

    final UUID uuid = dispenserRepository.save(dispenser);

    return new CreateDispenserResponse(uuid, flowVolume);
  }

  public void updateDispenserStatus(final UpdateDispenserStatusRequest updateDispenserStatusRequest, final UUID id) {
    final DispenserStatus newStatus = DispenserStatus.valueOf(updateDispenserStatusRequest.getStatus().toUpperCase());
    final ZonedDateTime updatedAt = updateDispenserStatusRequest.getUpdatedAt();
    final Dispenser dispenser = dispenserRepository.findById(id)
        .orElseThrow(() -> new DispenserNotFoundException("Dispenser with the Id " + id + " is not found"));

    DispenserSpendingLine dispenserSpendingLine = dispenserSpendingLineRepository.findLatestByDispenserId(id);

    // create new spending line record if none exists; also, if previous is closed, create new spending line
    if (dispenserSpendingLine == null || dispenserSpendingLine.getStatus() == CLOSE) {
      createNewDispenserLine(newStatus, updatedAt, dispenser);
    } else {
      // update existing spending line
      checkForStatusConflicts(dispenserSpendingLine, newStatus, updatedAt);
      dispenserSpendingLine.setStatus(newStatus);
      dispenserSpendingLine.setUpdatedAt(updatedAt);
      
      dispenserSpendingLineRepository.update(dispenserSpendingLine);
    }
  }

  public MoneySpentResponse calculateMoneySpent(final UUID id) {
    final Dispenser dispenser = dispenserRepository.findById(id)
        .orElseThrow(() -> new DispenserNotFoundException("Dispenser with the Id " + id + " is not found"));

    final List<DispenserSpendingLine> dispenserSpendingLine = dispenserSpendingLineRepository.findAllByDispenserId(id);

    return dispenserTransformer.transformToMoneySpentResponse(dispenser, dispenserSpendingLine);
  }

  private void createNewDispenserLine(final DispenserStatus newStatus, final ZonedDateTime updatedAt, final Dispenser dispenser) {
    DispenserSpendingLine dispenserSpendingLine;

    if (newStatus == CLOSE) {
      throw new DispenserStatusInConflictException("Dispenser is in conflicting status. Dispenser is already closed/never been opened.");
    }

    dispenserSpendingLine = DispenserSpendingLine.builder()
        .status(OPEN)
        .updatedAt(updatedAt)
        .dispenser(dispenser)
        .build();

    dispenserSpendingLineRepository.create(dispenserSpendingLine);
  }

  private void checkForStatusConflicts(final DispenserSpendingLine dispenserSpendingLine,
                                       final DispenserStatus newStatus, final ZonedDateTime updatedAt) {
    if (newStatus == CLOSE) {
      if (dispenserSpendingLine.getStatus() == CLOSE) {
        throw new DispenserStatusInConflictException("Dispenser is in conflicting status. It is is already closed.");
      } else if (updatedAt.isBefore(dispenserSpendingLine.getOpenedAt())) {
        throw new DispenserStatusInConflictException("Dispenser is in conflicting status. Closing time can't be before open time for the dispenser.");
      }
    }

    if (newStatus == OPEN && dispenserSpendingLine.getStatus() == OPEN) {
      throw new DispenserStatusInConflictException("Dispenser is in conflicting status. This dispenser is already opened.");
    }
  }
}
