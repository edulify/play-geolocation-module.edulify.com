package utils;

import com.edulify.modules.geolocation.Geolocation;

public class GeolocationHelper {
  public static boolean isReserved(String addr) {
    return "".equals(getCountryCode(addr));
  }

  private static String getCountryCode(String addr) {
//    Geolocation geolocation = //GeolocationService.getGeolocation(addr);
//    if (geolocation == null) {
      return null;
//    }
//    return geolocation.getCountryCode();
  }
}