package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationProvider;
import play.Configuration;
import play.libs.F;
import play.libs.ws.WSClient;
import play.mvc.Http;

public class MaxmindProvider implements GeolocationProvider, WSProvider, ConfigurableProvider {

  private static final String SERVICE_URL_PATTERN = "https://geoip.maxmind.com/a?l=%s&i=%s";
  private static final String RESPONSE_NOT_FOUND = "(null),IP_NOT_FOUND";

  private String license;
  private WSClient wsClient;

  public MaxmindProvider() {
  }

  @Override
  public void setClient(WSClient client) {
    this.wsClient = client;
  }

  @Override
  public void useConfiguration(Configuration configuration) {
    this.license = configuration.getString("geolocation.maxmind.license");
    if (null == license) {
      throw new IllegalStateException("Missing 'geolocation.maxmind.license' configuration parameter");
    }
  }

  @Override
  public F.Promise<Geolocation> get(final String ip) {
    String url = String.format(SERVICE_URL_PATTERN, license, ip);
    return wsClient.url(url)
        .get()
        .map(response -> {
          if (response.getStatus() != Http.Status.OK) return null;
          String body = response.getBody();
          if (RESPONSE_NOT_FOUND.equals(body)) return null;
          return body;
        })
        .map(body -> body == null ? new Geolocation() : new Geolocation(ip, body));
  }
}
