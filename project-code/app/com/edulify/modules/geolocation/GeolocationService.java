package com.edulify.modules.geolocation;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import play.Play;
import play.cache.Cache;
import play.libs.WS;
import play.Logger.ALogger;

public class GeolocationService {

  public enum Source {
    FREEGEOIP,
    GEOIP_COUNTRY
  }

  protected static boolean useCache      = Play.application().configuration().getBoolean("geolocation.useCache", true);
  protected static int cacheTTL          = Play.application().configuration().getInt("geolocation.cacheTTL", 3600);
  protected static boolean debug         = Play.application().configuration().getBoolean("geolocation.debug", false);
  protected static String maxmindLicense = Play.application().configuration().getString("geolocation.maxmind_license", "");
  public static Source source            = Source.valueOf(Play.application().configuration().getString("geolocation.source", "FREEGEOIP"));

  private static ALogger logger          = play.Logger.of("geolocation");

  public static void useCache(boolean useCache) {
    GeolocationService.useCache = useCache;
  }

  public static void setCacheTime(int seconds) {
    GeolocationService.cacheTTL = seconds;
  }

  public static void setSource(Source source) {
    GeolocationService.source = source;
  }

  public static void setMaxmindLicense(String license) {
    GeolocationService.maxmindLicense = license;
  }

  public static Geolocation getGeolocation(String ip) {
    return getGeolocation(ip, GeolocationService.source);
  }

  public static Geolocation getGeolocation(String ip, Source source) {
    String cacheKey = String.format("geolocation-cache-%s", ip);
    Geolocation geo = (Geolocation) Cache.get(cacheKey);
    if (useCache && geo != null) {
      return geo;
    }

    if (Source.FREEGEOIP.equals(source)) {
      geo = withFreegeoip(ip);
    }
    if (Source.GEOIP_COUNTRY.equals(source)) {
      geo = withGeoIpCountry(ip);
    }

    if (useCache) {
      Cache.set(cacheKey, geo, cacheTTL);
    }
    return geo;
  }

  private static Geolocation withFreegeoip(String ip) {
    String url = String.format("http://freegeoip.net/json/%s", ip);
    WS.WSRequest wsRequest = new WS.WSRequest("GET")
                                   .setUrl(url);

    try {
      if (debug) {
        logger.debug(String.format("requesting %s using freegeoip...", ip));
      }
      WS.Response response = wsRequest.execute().get(5000l); // Don't wait more than 5 seconds for service response.

      String responseBody  = response.getBody();
      if (debug) {
        logger.debug(String.format("response: %s", responseBody));
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

      return new Geolocation(jsonIp.getTextValue(),
                             jsonCountryCode.getTextValue(),
                             jsonCountryName.getTextValue(),
                             jsonRegionCode.getTextValue(),
                             jsonRegionName.getTextValue(),
                             jsonCity.getTextValue(),
                             jsonLatitude.getDoubleValue(),
                             jsonLongitude.getDoubleValue());
    } catch (InvalidAddressException ex) {
      throw ex;
    } catch (Exception ex) {
      logger.error("Exception ", ex);
      ex.printStackTrace();
    }
    return null;
  }

  private static Geolocation withGeoIpCountry(String ip) {
    String url = String.format("https://geoip.maxmind.com/a?l=%s&i=%s", maxmindLicense, ip);
    WS.WSRequest wsRequest = new WS.WSRequest("GET")
                                   .setUrl(url);
    try {
      if (debug) {
        logger.debug(String.format("requesting %s using geoip_country...", ip));
      }
      WS.Response response = wsRequest.execute().get(5000l); // Don't wait more than 5 seconds for service response.

      String responseBody  = response.getBody().trim();
      if (debug) {
        logger.debug(String.format("response: %s", responseBody));
      }
      if ("(null),IP_NOT_FOUND".equals(responseBody)) {
        throw new InvalidAddressException(String.format("Invalid address: %s", ip));
      }
      if (responseBody.length() == 2) {
        return new Geolocation(ip,
                               responseBody,
                               null,
                               null,
                               null,
                               null,
                               0.0,
                               0.0);
      }
      throw new ServiceErrorException(String.format("Unknown service response: %s", responseBody));
    } catch (InvalidAddressException | ServiceErrorException ex) {
      throw ex;
    } catch (Exception ex) {
      logger.error("Exception ", ex);
      ex.printStackTrace();
    }
    return null;
  }
}