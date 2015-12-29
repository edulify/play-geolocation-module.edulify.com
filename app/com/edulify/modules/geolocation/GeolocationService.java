package com.edulify.modules.geolocation;

import play.libs.F;

import javax.inject.Inject;

public final class GeolocationService {

  private GeolocationProvider provider;
  private GeolocationCache cache;

  @Inject
  public GeolocationService(GeolocationProvider provider, GeolocationCache cache) {
    this.provider = provider;
    this.cache = cache;
  }

  public F.Promise<Geolocation> getGeolocation(String ip) {
    Geolocation geolocation = cache.get(ip);
    if (geolocation != null) return F.Promise.pure(geolocation);

    F.Promise<Geolocation> promise = provider.get(ip);
    promise.onRedeem(gl -> cache.set(gl));
    return promise;
  }
}
