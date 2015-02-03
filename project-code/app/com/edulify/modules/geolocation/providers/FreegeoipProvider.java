package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationProvider;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

public class FreegeoipProvider implements GeolocationProvider {
  
  @Override
  public F.Promise<Geolocation> get(String ip) {
    String url = String.format("http://freegeoip.net/json/%s", ip);
    return WS.url(url)
        .get()
        .map(new F.Function<WSResponse, JsonNode>() {
          @Override
          public JsonNode apply(WSResponse response) throws Throwable {
            if (response.getStatus() != 200) return null;
            if (response.getBody().contains("not found")) return null;
            return response.asJson();
          }
        })
        .map(new F.Function<JsonNode, Geolocation>() {
          @Override
          public Geolocation apply(JsonNode json) throws Throwable {
            if (json == null) return Geolocation.empty();
            return asGeolocation(json);
          }
        });
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

    if (jsonIp          == null ||
        jsonCountryCode == null ||
        jsonCountryName == null ||
        jsonRegionCode  == null ||
        jsonRegionName  == null ||
        jsonCity        == null ||
        jsonLatitude    == null ||
        jsonLongitude   == null) {
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
        jsonLongitude.asDouble()
    );
  }
}