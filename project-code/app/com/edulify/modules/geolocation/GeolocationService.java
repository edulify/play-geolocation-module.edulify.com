package com.edulify.modules.geolocation;

import play.libs.F.Promise;

import static play.libs.F.Promise.pure;

public final class GeolocationService {

  private final GeolocationCache cache;
  private final GeolocationProvider provider;

  public GeolocationService(GeolocationCache cache, GeolocationProvider provider) {
    this.cache = cache;
    this.provider = provider;
  }

  public Promise<Geolocation> getGeolocation(String ip) {
    if (null == ip) return pure(null);
    if (ip.isEmpty()) return pure(null);
    Geolocation geolocation = cache.get(ip);
    if (geolocation != null) return pure(geolocation);
    
    Promise<Geolocation> promise = provider.get(ip);
    promise.onRedeem(cache::set);
    return promise;
  }
}
