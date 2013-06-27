package com.edulify.modules.geolocation;

public class ServiceErrorException extends RuntimeException {
  public ServiceErrorException(String arg, Throwable cause) {
    super(arg, cause);
  }

  public ServiceErrorException(String arg) {
    super(arg);
  }

  public ServiceErrorException() {
    super();
  }
}