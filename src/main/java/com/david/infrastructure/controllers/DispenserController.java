package com.david.infrastructure.controllers;

import com.david.application.DispenserService;
import com.david.application.request.CreateDispenserRequest;
import com.david.application.request.UpdateDispenserStatusRequest;
import com.david.application.response.CreateDispenserResponse;
import com.david.application.response.MoneySpentResponse;
import com.david.domain.exception.DispenserNotFoundException;
import com.david.domain.exception.DispenserStatusInConflictException;
import com.david.infrastructure.controllers.validator.CreateDispenserValidator;
import com.david.infrastructure.controllers.validator.UpdateDispenserStatusValidator;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by david on 07-03-2023
 */

@RestController
@AllArgsConstructor
public class DispenserController {

  private final DispenserService dispenserService;

  @InitBinder("createDispenserRequest")
  void initBinder(final WebDataBinder binder) {
    binder.addValidators(new CreateDispenserValidator());
  }

  @InitBinder("updateDispenserStatusRequest")
  void initBinder2(final WebDataBinder binder) {
    binder.addValidators(new UpdateDispenserStatusValidator());
  }

  @PostMapping("/dispenser")
  public ResponseEntity<CreateDispenserResponse> createDispenser(@Valid @RequestBody CreateDispenserRequest createDispenserRequest) {
    return new ResponseEntity<>(dispenserService.createDispenser(createDispenserRequest), HttpStatus.OK);
  }

  @PutMapping("/dispenser/{id}/status")
  public ResponseEntity<String> updateDispenserStatus(@Valid @RequestBody UpdateDispenserStatusRequest updateDispenserStatusRequest,
                                                      @PathVariable UUID id) {

    try {
      dispenserService.updateDispenserStatus(updateDispenserStatusRequest, id);
    } catch (DispenserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    } catch (DispenserStatusInConflictException ex2) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ex2.getMessage());
    }

    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @GetMapping("/dispenser/{id}/spending")
  public ResponseEntity<MoneySpentResponse> calculateMoneySpent(@PathVariable UUID id) {
    return new ResponseEntity<>(dispenserService.calculateMoneySpent(id), HttpStatus.OK);
  }

}
