package com.edulify.modules.geolocation;

import play.libs.F;

public interface GeolocationProvider {

  /**
   * Get the geolocation to a given ip
   * @param ip IP to locate
   * @return Geolocation
   */
  F.Promise<Geolocation> get(String ip);
}
