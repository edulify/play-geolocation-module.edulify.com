package com.edulify.modules.geolocation;

public class Geolocation {

  private String ip;
  private String countryCode;
  private String countryName;
  private String regionCode;
  private String regionName;
  private String city;
  private double latitude;
  private double longitude;

  public Geolocation(String ip,
                     String countryCode,
                     String countryName,
                     String regionCode,
                     String regionName,
                     String city,
                     double latitude,
                     double longitude) {

    this.ip          = ip;
    this.countryCode = countryCode;
    this.countryName = countryName;
    this.regionCode  = regionCode;
    this.regionName  = regionName;
    this.city        = city;
    this.latitude    = latitude;
    this.longitude   = longitude;
  }

  public String getIp() {
    return this.ip;
  }

  public String getCountryCode() {
    return this.countryCode;
  }

  public String getCountryName() {
    return this.countryName;
  }

  public String getRegionCode() {
    return this.regionCode;
  }

  public String getRegionName() {
    return this.regionName;
  }

  public String getCity() {
    return this.city;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public double getLongitude() {
    return this.longitude;
  }

  @Override
  public String toString() {
    return String.format("ip: %s, countryCode: %s, countryName: %s, regionCode: %s, regionName: %s, city: %s, latitude: %s, longitude: %s",
                         this.ip,
                         this.countryCode,
                         this.countryName,
                         this.regionCode,
                         this.regionName,
                         this.city,
                         this.latitude,
                         this.longitude);
  }

}