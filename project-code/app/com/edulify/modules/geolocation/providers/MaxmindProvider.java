package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationFactory;
import com.edulify.modules.geolocation.GeolocationProvider;
import play.libs.F;
import play.libs.ws.WSClient;
import play.mvc.Http;

public class MaxmindProvider implements GeolocationProvider {

  private static final String SERVICE_URL_PATTERN = "https://geoip.maxmind.com/a?l=%s&i=%s";
  private static final String RESPONSE_NOT_FOUND = "(null),IP_NOT_FOUND";

  private final String license;
  private final WSClient wsClient;
  private final GeolocationFactory factory;

  public MaxmindProvider(WSClient wsClient, GeolocationFactory factory, String license) {
    this.wsClient = wsClient;
    this.factory = factory;
    this.license = license;
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
        .map(body -> body == null ? factory.create() : factory.create(ip, body));
  }
}
