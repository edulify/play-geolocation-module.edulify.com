package com.edulify.modules.geolocation;

/**
 * @deprecated Use AsyncGeolocationService instead
 */
@Deprecated
public class GeolocationService {

  private static final long timeout = Config.getMillisecondsOr("geolocation.timeout", 5000);

  public static Geolocation getGeolocation(String ip) {
    return AsyncGeolocationService.getGeolocation(ip).get(timeout);
  }
}