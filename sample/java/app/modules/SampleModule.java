package modules;

import com.edulify.modules.geolocation.GeolocationFactory;
import com.edulify.modules.geolocation.GeolocationProvider;
import com.edulify.modules.geolocation.providers.FreegeoipProvider;
import com.google.inject.AbstractModule;
import play.libs.ws.WSClient;

/**
 * Created by sowhat
 */
public class SampleModule extends AbstractModule {
  @Override
  protected void configure() {
    try {
      bind(GeolocationProvider.class).toConstructor(FreegeoipProvider.class.getConstructor(WSClient.class, GeolocationFactory.class));
    } catch (NoSuchMethodException e) {
      addError(e);
    }
  }
}
