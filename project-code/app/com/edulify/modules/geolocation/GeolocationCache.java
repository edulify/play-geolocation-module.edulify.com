package com.edulify.modules.geolocation;

import play.cache.Cache;

import static com.edulify.modules.geolocation.Config.getBooleanOr;
import static com.edulify.modules.geolocation.Config.getMillisecondsOr;

public class GeolocationCache {

  private final boolean cacheOn;
  private final long cacheTtl;

  public GeolocationCache() {
    cacheOn = getBooleanOr("geolocation.cache.on", false);
    cacheTtl = getMillisecondsOr("geolocation.cache.ttl", 5000);
  }

  public void set(Geolocation geolocation) {
    if (!cacheOn) return;
    Cache.set(key(geolocation.getIp()), geolocation, (int)cacheTtl);
  }

  public Geolocation get(String ip) {
    if (!cacheOn) return null;
    return (Geolocation) Cache.get(key(ip));
  }

  private String key(String ip) {
    return "geolocation-cache-" + ip;
  }
}
