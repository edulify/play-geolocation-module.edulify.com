package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationFactory;
import com.edulify.modules.geolocation.GeolocationProvider;
import play.libs.F;
import play.libs.ws.WSClient;
import play.mvc.Http;

import static com.edulify.modules.geolocation.Config.getString;

public class MaxmindProvider implements GeolocationProvider {

  private static final String SERVICE_URL_PATTERN = "https://geoip.maxmind.com/a?l=%s&i=%s";
  private static final String RESPONSE_NOT_FOUND = "(null),IP_NOT_FOUND";
  private static final String LICENSE = getString("geolocation.maxmind.license");

  private final WSClient wsClient;
  private final GeolocationFactory factory;

  public MaxmindProvider(WSClient wsClient, GeolocationFactory factory) {
    this.wsClient = wsClient;
    this.factory = factory;
  }

  @Override
  public F.Promise<Geolocation> get(final String ip) {
    String url = String.format(SERVICE_URL_PATTERN, LICENSE, ip);
    return wsClient.url(url)
        .get()
        .map(response -> {
          if (response.getStatus() != Http.Status.OK) return null;
          String body = response.getBody();
          if (RESPONSE_NOT_FOUND.equals(body)) return null;
          return body;
        })
        .map(body -> body == null ? factory.create() : factory.create(ip, body));
  }
}
