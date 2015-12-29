package com.edulify.modules.geolocation;

import play.Configuration;
import play.cache.CacheApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeolocationCache {

  private boolean cacheOn;
  private long cacheTtl;

  private Configuration configuration;
  private CacheApi cache;

  @Inject
  public GeolocationCache(Configuration configuration, CacheApi cache) {
    this.configuration = configuration;
    this.cache = cache;
    this.init();
  }

  private void init() {
    this.cacheOn = configuration.getBoolean("geolocation.cache.on", false);
    this.cacheTtl = configuration.getMilliseconds("geolocation.cache.ttl", 5000L);
  }

  public void set(Geolocation geolocation) {
    if (!cacheOn) return;
    cache.set(key(geolocation.getIp()), geolocation, (int)cacheTtl);
  }

  public Geolocation get(String ip) {
    if (!cacheOn) return null;
    return (Geolocation) cache.get(key(ip));
  }

  private String key(String ip) {
    return "geolocation-cache-" + ip;
  }
}
