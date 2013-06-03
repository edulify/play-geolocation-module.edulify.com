package com.edulify.modules.geolocation;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import play.Play;
import play.cache.Cache;
import play.libs.WS;

public class GeolocationService {

  protected static boolean useCache = Play.application().configuration().getBoolean("geolocation.useCache", true);
  protected static int cacheTTL     = Play.application().configuration().getInt("geolocation.cacheTTL", 3600);
  protected static boolean debug    = Play.application().configuration().getBoolean("geolocation.debug", false);

  public static void useCache(boolean useCache) {
    GeolocationService.useCache = useCache;
  }

  public static void setCacheTime(int seconds) {
    GeolocationService.cacheTTL = seconds;
  }

  public static Geolocation getGeolocation(String ip) {
    return getGeolocation(ip, false);
  }

  public static Geolocation getGeolocation(String ip, boolean ignoreCache) {
    String cacheKey = String.format("geolocation-cache-%s", ip);
    Geolocation geo = (Geolocation) Cache.get(cacheKey);
    if (!ignoreCache && useCache && geo != null) {
      return geo;
    }

    String url = String.format("http://freegeoip.net/json/%s", ip);
    WS.WSRequest wsRequest = new WS.WSRequest("GET")
                                   .setUrl(url);

    try {
      if (debug) {
        play.Logger.debug(String.format("requesting %s...", ip));
      }
      WS.Response response = wsRequest.execute().get(5000l); // Don't wait more than 5 seconds for service response.

      String responseBody  = response.getBody();
      if (debug) {
        play.Logger.debug(String.format("response: %s", responseBody));
      }
      if ("Not Found".equals(responseBody.trim())) {
        throw new InvalidAddressException(String.format("Invalid address: %s", ip));
      }

      JsonNode jsonResponse = response.asJson();

      JsonNode jsonIp          = jsonResponse.get("ip");
      JsonNode jsonCountryCode = jsonResponse.get("country_code");
      JsonNode jsonCountryName = jsonResponse.get("country_name");
      JsonNode jsonRegionCode  = jsonResponse.get("region_code");
      JsonNode jsonRegionName  = jsonResponse.get("region_name");
      JsonNode jsonCity        = jsonResponse.get("city");
      JsonNode jsonLatitude    = jsonResponse.get("latitude");
      JsonNode jsonLongitude   = jsonResponse.get("longitude");

      if (jsonIp          == null ||
          jsonCountryCode == null ||
          jsonCountryName == null ||
          jsonRegionCode  == null ||
          jsonRegionName  == null ||
          jsonCity        == null ||
          jsonLatitude    == null ||
          jsonLongitude   == null) {
        return null;
      }

      geo = new Geolocation(jsonIp.getTextValue(),
                            jsonCountryCode.getTextValue(),
                            jsonCountryName.getTextValue(),
                            jsonRegionCode.getTextValue(),
                            jsonRegionName.getTextValue(),
                            jsonCity.getTextValue(),
                            jsonLatitude.getDoubleValue(),
                            jsonLongitude.getDoubleValue());
      if (useCache) {
        Cache.set(cacheKey, geo, cacheTTL);
      }
      return geo;
    } catch (InvalidAddressException ex) {
      throw ex;
    } catch (Exception ex) {
      play.Logger.error("Exception ", ex);
      ex.printStackTrace();
    }
    return null;
  }

}