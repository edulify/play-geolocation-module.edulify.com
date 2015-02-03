package com.edulify.modules.geolocation;

import play.libs.F;

public final class AsyncGeolocationService {
  
  private static GeolocationProvider provider;
  
  public static void initialize(GeolocationProvider provider) {
    AsyncGeolocationService.provider = provider;
  }
  
  public static F.Promise<Geolocation> getGeolocation(String ip) {
    Geolocation geolocation = GeolocationCache.get(ip);
    if (geolocation != null) return F.Promise.pure(geolocation);
    
    F.Promise<Geolocation> promise = provider.get(ip);
    promise.onRedeem(new F.Callback<Geolocation>() {
      @Override
      public void invoke(Geolocation geolocation) throws Throwable {
        GeolocationCache.set(geolocation);
      }
    });
    return promise;
  }
}
