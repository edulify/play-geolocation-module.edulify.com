package modules;

import com.edulify.modules.geolocation.GeolocationProvider;
import com.edulify.modules.geolocation.providers.FreegeoipProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import play.libs.ws.WSClient;

/**
 * Created by sowhat
 */
public class SampleModule extends AbstractModule {
  @Override
  protected void configure() {
  }

  @Provides
  @Inject
  private GeolocationProvider geolocationProvider(WSClient client)
  {
    FreegeoipProvider provider = new FreegeoipProvider();
    provider.setClient(client);
    return provider;
  }
}
