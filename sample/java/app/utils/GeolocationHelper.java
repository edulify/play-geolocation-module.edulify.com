package utils;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;
import com.edulify.modules.geolocation.GeolocationService.Source;

public class GeolocationHelper {
  public static boolean isReserved(String addr) {
    if (GeolocationService.source.equals(Source.FREEGEOIP)) {
      return "RD".equals(getCountryCode(addr));
    }
    return false;
  }

  public static String getCountry(String addr) {
    Geolocation geolocation = getGeolocation(addr);
    if (geolocation == null) {
      return null;
    }
    return geolocation.getCountryName();
  }

  public static String getCountryCode(String addr) {
    Geolocation geolocation = getGeolocation(addr);
    if (geolocation == null) {
      return null;
    }
    return geolocation.getCountryCode();
  }

  public static Geolocation getGeolocation(String addr) {
    return GeolocationService.getGeolocation(addr);
  }
}