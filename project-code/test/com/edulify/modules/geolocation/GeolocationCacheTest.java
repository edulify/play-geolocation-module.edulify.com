package com.edulify.modules.geolocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static play.test.Helpers.*;
import static org.junit.Assert.*;

/**
 * Created by sovaalexandr
 */
@RunWith(MockitoJUnitRunner.class)
public class GeolocationCacheTest extends WithApplication {
  @Mock
  private Geolocation mockGeolocation;
  @Test
  public void testSetGet() throws Exception {
    Map<String, Object> configMap = new HashMap<>(1);
    configMap.put("geolocation.cache.on", true);
    running(fakeApplication(configMap), new Runnable() {
      @Override
      public void run() {
        String ipAddress = "192.168.0.1";
        when(mockGeolocation.getIp()).thenReturn(ipAddress);
        GeolocationCache.set(mockGeolocation);
        assertSame(mockGeolocation, GeolocationCache.get(ipAddress));
      }
    });
  }
}