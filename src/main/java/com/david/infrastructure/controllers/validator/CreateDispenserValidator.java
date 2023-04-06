package com.david.infrastructure.controllers.validator;

import com.david.application.request.CreateDispenserRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by david on 07-03-2023
 */

public class CreateDispenserValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return CreateDispenserRequest.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    final CreateDispenserRequest request = (CreateDispenserRequest) target;

    if (request.getFlowVolume() <= 0) {
      errors.rejectValue("flowVolume", "invalid.flowVolume",
                         "Please specify valid flow volume. Flow_volume is the number of liters per second that are coming out from the tap.");
    }

  }
}
