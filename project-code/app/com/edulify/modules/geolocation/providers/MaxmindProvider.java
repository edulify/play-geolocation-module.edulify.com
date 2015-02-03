package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Config;
import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationProvider;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

public class MaxmindProvider implements GeolocationProvider {
  
  private static final String license = Config.getString("geolocation.maxmind.license");
  
  @Override
  public F.Promise<Geolocation> get(final String ip) {
    String url = String.format("https://geoip.maxmind.com/a?l=%s&i=%s", license, ip);
    return WS.url(url)
        .get()
        .map(new F.Function<WSResponse, String>() {
          @Override
          public String apply(WSResponse response) throws Throwable {
            if (response.getStatus() != 200) return null;
            
            String body = response.getBody();
            if ("(null),IP_NOT_FOUND".equals(body)) return null;
            return body;
          }
        })
        .map(new F.Function<String, Geolocation>() {
          @Override
          public Geolocation apply(String body) throws Throwable {
            if (body == null) return Geolocation.empty();
            return new Geolocation(ip, body);
          }
        });
  }
}
