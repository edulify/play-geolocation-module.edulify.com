package com.edulify.modules.geolocation;

/**
 * @deprecated Use AsyncGeolocationService instead
 */
@Deprecated
public class GeolocationService {

  private static final long timeout = Config.getMillisecondsOr("geolocation.timeout", 5000);

  private static GeolocationProvider provider;

  public static void initialize(GeolocationProvider provider) {
    GeolocationService.provider = provider;
  }

  public static Geolocation getGeolocation(String ip) {
    Geolocation geolocation = GeolocationCache.get(ip);
    if (geolocation != null)  return geolocation;
    geolocation = provider.get(ip).get(timeout);
    GeolocationCache.set(geolocation);
    return geolocation;
  }
}