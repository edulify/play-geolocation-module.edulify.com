package com.edulify.modules.geolocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.test.WithApplication;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GeolocationCacheTest extends WithApplication {

  @Mock
  private Geolocation mockGeolocation;

  @Test
  public void testSetGet() throws Exception {
    final String ipAddress = "192.168.0.1";
    when(mockGeolocation.getIp()).thenReturn(ipAddress);
    GeolocationCache service = new GeolocationCache(true, 5000);
    service.set(mockGeolocation);
    assertSame(mockGeolocation, service.get(ipAddress));
  }
}
