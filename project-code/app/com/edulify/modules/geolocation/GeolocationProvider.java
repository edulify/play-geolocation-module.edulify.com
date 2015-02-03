package com.edulify.modules.geolocation;

import play.libs.F;

public interface GeolocationProvider {

  /**
   * Get the geolocation to a given ip
   */
  F.Promise<Geolocation> get(String ip);
}
