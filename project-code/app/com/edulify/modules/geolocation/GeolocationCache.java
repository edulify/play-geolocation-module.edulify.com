package com.edulify.modules.geolocation;

import play.cache.Cache;

public class GeolocationCache {

  private final boolean cacheOn;
  private final long cacheTtl;

  public GeolocationCache(boolean cacheOn, long cacheTtl) {
    this.cacheOn = cacheOn;
    this.cacheTtl = cacheTtl;
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
