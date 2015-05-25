package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationFactory;
import com.edulify.modules.geolocation.GeolocationProvider;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.mvc.Http;

public class FreegeoipProvider implements GeolocationProvider {
  private static final String SERVICE_URL_INDEX = "http://freegeoip.net/json/";
  private static final String RESPONSE_NOT_FOUND = "not found";

  private final WSClient wsClient;
  private final GeolocationFactory factory;

  public FreegeoipProvider(WSClient wsClient, GeolocationFactory factory) {
    this.wsClient = wsClient;
    this.factory = factory;
  }

  @Override
  public Promise<Geolocation> get(final String ip) {
    String url = SERVICE_URL_INDEX + ip;
    return wsClient.url(url)
        .get()
        .map(response -> {
          if (response.getStatus() != Http.Status.OK) return null;
          String body = response.getBody();
          if (body.contains(RESPONSE_NOT_FOUND)) return null;
          return response.asJson();
        })
        .map(json -> json == null ? factory.create() : asGeolocation(json));
  }

  private Geolocation asGeolocation(JsonNode json) {
    JsonNode jsonIp          = json.get("ip");
    JsonNode jsonCountryCode = json.get("country_code");
    JsonNode jsonCountryName = json.get("country_name");
    JsonNode jsonRegionCode  = json.get("region_code");
    JsonNode jsonRegionName  = json.get("region_name");
    JsonNode jsonCity        = json.get("city");
    JsonNode jsonLatitude    = json.get("latitude");
    JsonNode jsonLongitude   = json.get("longitude");
    JsonNode jsonTimeZone    = json.get("time_zone");

    if (jsonIp          == null ||
        jsonCountryCode == null ||
        jsonCountryName == null ||
        jsonRegionCode  == null ||
        jsonRegionName  == null ||
        jsonCity        == null ||
        jsonLatitude    == null ||
        jsonLongitude   == null ||
        jsonTimeZone    == null) {
      return factory.create();
    }

    return factory.create(
        jsonIp.asText(),
        jsonCountryCode.asText(),
        jsonCountryName.asText(),
        jsonRegionCode.asText(),
        jsonRegionName.asText(),
        jsonCity.asText(),
        jsonLatitude.asDouble(),
        jsonLongitude.asDouble(),
        jsonTimeZone.asText()
    );
  }
}