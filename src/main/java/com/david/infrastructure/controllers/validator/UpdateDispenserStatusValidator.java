package com.david.infrastructure.controllers.validator;

import com.david.application.request.UpdateDispenserStatusRequest;
import com.david.domain.model.DispenserStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by david on 08-03-2023
 */

public class UpdateDispenserStatusValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return UpdateDispenserStatusRequest.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    final UpdateDispenserStatusRequest request = (UpdateDispenserStatusRequest) target;

    try {
      DispenserStatus.valueOf(request.getStatus().toUpperCase());
    } catch (IllegalArgumentException ex) {
      errors.rejectValue("status", "invalid.status", "Please provide a valid status for the request. "
          + "The status can only be either \"open\" or \"close\".");
    }

  }
}
