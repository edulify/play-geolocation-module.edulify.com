package com.edulify.modules.geolocation;

import java.util.concurrent.CompletionStage;

public interface GeolocationProvider {

  /**
   * Get the geolocation to a given ip
   */
  CompletionStage<Geolocation> get(String ip);
}
