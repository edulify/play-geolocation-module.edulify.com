package com.edulify.modules.geolocation;

import org.junit.Test;
import play.test.WithApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

/**
 * Created by sovaalexandr
 */
public class GeolocationServiceTest extends WithApplication {

  @Test
  public void testGetGeolocation() throws Exception {
    Map<String, Object> config = new HashMap<>(1);
    config.put("geolocation.provider", "com.edulify.modules.geolocation.providers.FreegeoipProvider");
    List<String> plugins = new ArrayList<>(1);
    plugins.add(GeolocationPlugin.class.getCanonicalName());
    running(fakeApplication(config, plugins), new Runnable() {
      @Override
      public void run() {
        String ipAddress = "192.30.252.129";
        Geolocation geolocation = GeolocationService.getGeolocation(ipAddress);
        assertEquals(ipAddress, geolocation.getIp());
      }
    });
  }
}
