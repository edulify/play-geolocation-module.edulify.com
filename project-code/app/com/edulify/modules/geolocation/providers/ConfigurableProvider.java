package com.edulify.modules.geolocation.providers;

import play.Configuration;

/**
 * Created by sowhat
 */
public interface ConfigurableProvider {
  void useConfiguration(Configuration configuration);
}
