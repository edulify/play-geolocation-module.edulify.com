package com.edulify.modules.geolocation;

public class GeolocationFactory {
  public Geolocation create(String ip,
                            String countryCode,
                            String countryName,
                            String regionCode,
                            String regionName,
                            String city,
                            double latitude,
                            double longitude,
                            String timeZone) {
    return new Geolocation(ip, countryCode, countryName, regionCode, regionName, city, latitude, longitude, timeZone);
  }
  public Geolocation create(String ip,
                            String countryCode,
                            String countryName,
                            String regionCode,
                            String regionName,
                            String city) {
    return create(ip, countryCode, countryName, regionCode, regionName, city, .0, .0, null);
  }

  public Geolocation create(String ip, String countryCode) {
    return create(ip, countryCode, "", "", "", "");
  }

  public Geolocation create() {
    return create("", "");
  }
}
