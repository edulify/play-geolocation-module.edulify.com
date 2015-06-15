package com.edulify.modules.geolocation;

import com.edulify.modules.geolocation.providers.ConfigurableProvider;
import com.edulify.modules.geolocation.providers.FreegeoipProvider;
import com.edulify.modules.geolocation.providers.WSProvider;
import play.Application;
import play.Plugin;
import play.libs.ws.WS;
import play.libs.ws.WSClient;

import java.lang.reflect.InvocationTargetException;

@Deprecated
public class GeolocationPlugin extends Plugin {

  private final Application application;

  public GeolocationPlugin(Application application) {
    this.application = application;
  }

  @Override
  public void onStart() {
    super.onStart();
    try {
      String providerClassName = application.configuration().getString("geolocation.provider");
      Class<?> providerClass = null == providerClassName
          ? FreegeoipProvider.class
          : Class.forName(providerClassName);
      play.Logger.info("Starting GeolocationPlugin with provider " + providerClass);
      GeolocationProvider provider = (GeolocationProvider) providerClass.newInstance();
      if (provider instanceof WSProvider) {
        ((WSProvider) provider).setClient(WS.client());
      }
      if (provider instanceof ConfigurableProvider) {
        ((ConfigurableProvider) provider).useConfiguration(application.configuration());
      }
      GeolocationService service = new GeolocationService(
          new GeolocationCache(
              application.configuration().getBoolean("geolocation.cache.on"),
              application.configuration().getMilliseconds("geolocation.cache.ttl", 5000l)
          ),
          provider
      );
      AsyncGeolocationService.initialize(service);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Could not initialize GeolocationPlugin. Check if class configured in geolocation.provider exists", ex);
    } catch (IllegalAccessException | InstantiationException ex) {
      throw new RuntimeException("Could not initialize GeolocationPlugin. Check if class configured in geolocation.provider has a public default constructor", ex);
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    play.Logger.info("Stopping GeolocationPlugin...");
  }

  @Override
  public boolean enabled() {
    return application.configuration().getBoolean("geolocation.enabled", true);
  }
}
