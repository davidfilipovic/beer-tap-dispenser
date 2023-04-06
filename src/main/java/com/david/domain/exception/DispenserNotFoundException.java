package com.david.domain.exception;

/**
 * Created by david on 07-03-2023
 */

public class DispenserNotFoundException extends RuntimeException {
  public DispenserNotFoundException(String message) {
    super(message);
  }
}
