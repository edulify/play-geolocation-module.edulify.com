package com.edulify.modules.geolocation;

import play.Application;

public class GeolocationPlugin extends play.Plugin {

  public GeolocationPlugin(Application application) {
    // do nothing
  }

  @Override
  public void onStart() {
    super.onStart();
    try {
      Class<?> providerClass = Class.forName(Config.getStringOr("geolocation.provider", "com.edulify.modules.geolocation.providers.FreegeoipProvider"));
      play.Logger.info("Starting GeolocationPlugin with provider " + providerClass);
      GeolocationProvider provider = (GeolocationProvider) providerClass.newInstance();
      AsyncGeolocationService.initialize(provider);
      GeolocationService.initialize(provider);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Could not initialize GeolocationPlugin. Check if class configured in geolocation.provider exists", ex);
    } catch (InstantiationException ex) {
      throw new RuntimeException("Could not initialize GeolocationPlugin. Check if class configured in geolocation.provider has a default constructor", ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException("Could not initialize GeolocationPlugin. Check if class configured in geolocation.provider has a public constructor", ex);
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    play.Logger.info("Stopping GeolocationPlugin...");
  }

  @Override
  public boolean enabled() {
    return Config.getBooleanOr("geolocation.enabled", true);
  }
}
