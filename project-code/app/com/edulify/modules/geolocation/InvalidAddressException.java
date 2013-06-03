package com.edulify.modules.geolocation;

public class InvalidAddressException extends RuntimeException {
  public InvalidAddressException(String arg, Throwable cause) {
    super(arg, cause);
  }

  public InvalidAddressException(String arg) {
    super(arg);
  }

  public InvalidAddressException() {
    super();
  }
}