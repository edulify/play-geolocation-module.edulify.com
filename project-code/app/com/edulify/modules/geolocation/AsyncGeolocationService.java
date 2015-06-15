package com.edulify.modules.geolocation;

import play.libs.F;

@Deprecated
public final class AsyncGeolocationService {

  private static GeolocationService service;

  public static void initialize(GeolocationService service) {
    AsyncGeolocationService.service = service;
  }

  public static F.Promise<Geolocation> getGeolocation(String ip) {
    if (null == service) {
      throw new IllegalStateException("Service not initialized. Enable GeolocationPlugin to use it.");
    } else {
      return service.getGeolocation(ip);
    }
  }
}
