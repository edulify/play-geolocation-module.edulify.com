package com.edulify.modules.geolocation;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import play.Application;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

public class GeolocationCacheTest {

  private final String ipAddress = "192.30.252.129";
  private final String countryCode = "BR";

  @Test
  public void shouldAddGeolocationToCacheWhenCacheIsOn() {
    Application application = getApplication(true);

    Helpers.running(application, () -> {
      Geolocation geolocation = new Geolocation(ipAddress, countryCode);
      GeolocationCache cache = application.injector().instanceOf(GeolocationCache.class);

      cache.set(geolocation);
      Assert.assertThat(cache.get(ipAddress), CoreMatchers.notNullValue());
    });
  }

  @Test
  public void shouldNotAddGeolocationToCacheWhenCacheIsOff() {
    Application application = getApplication(false);

    Helpers.running(application, () -> {
      Geolocation geolocation = new Geolocation(ipAddress, countryCode);
      GeolocationCache cache = application.injector().instanceOf(GeolocationCache.class);

      cache.set(geolocation);
      Assert.assertThat(cache.get(ipAddress), CoreMatchers.nullValue());
    });
  }

  private Application getApplication(boolean cacheOn) {
    Map<String, Object> config = new HashMap<>();
    config.put("geolocation.cache.on", cacheOn);
    return Helpers.fakeApplication(config);
  }
}
