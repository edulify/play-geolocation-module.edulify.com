package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationProvider;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.concurrent.HttpExecution;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

@Singleton
public class FreegeoipProvider implements GeolocationProvider {

  private WSClient ws;

  @Inject
  public FreegeoipProvider(WSClient ws) {
    this.ws = ws;
  }

  @Override
  public CompletionStage<Geolocation> get(String ip) {
    String url = String.format("http://freegeoip.net/json/%s", ip);
    return ws.url(url)
        .get()
        .thenApplyAsync(response -> {
          if (response.getStatus() != 200) return null;
          if (response.getBody().contains("not found")) return null;
          return response.asJson();
        }, HttpExecution.defaultContext())
        .thenApplyAsync(json -> {
          if (json == null) return Geolocation.empty();
          return asGeolocation(json);
        }, HttpExecution.defaultContext());
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
      return Geolocation.empty();
    }

    return new Geolocation(
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