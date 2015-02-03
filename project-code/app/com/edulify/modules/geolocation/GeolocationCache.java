package com.edulify.modules.geolocation;

import play.cache.Cache;

public class GeolocationCache {
  
  private static final boolean cacheOn = Config.getBooleanOr("geolocation.cache.on", false);
  private static final long cacheTtl = Config.getMillisecondsOr("geolocation.cache.ttl", 5000);
  
  public static void set(Geolocation geolocation) {
    if (!cacheOn) return;
    Cache.set(key(geolocation.getIp()), geolocation, (int)cacheTtl);
  }
  
  public static Geolocation get(String ip) {
    if (!cacheOn) return null;
    return (Geolocation) Cache.get(ip);
  }
  
  private static String key(String ip) {
    return "geolocation-cache-" + ip;
  }
}
