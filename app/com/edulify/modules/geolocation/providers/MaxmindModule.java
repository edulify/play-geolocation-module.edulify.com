package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.GeolocationProvider;
import com.google.inject.AbstractModule;

public class MaxmindModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(GeolocationProvider.class).to(MaxmindProvider.class);
  }
}
