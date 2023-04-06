package com.david.domain.exception;

/**
 * Created by david on 07-03-2023
 */

public class DispenserStatusInConflictException extends RuntimeException {
  public DispenserStatusInConflictException(String message) {
    super(message);
  }
}
