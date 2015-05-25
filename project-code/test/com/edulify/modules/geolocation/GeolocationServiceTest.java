package com.edulify.modules.geolocation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.libs.F.Promise;
import play.test.WithApplication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static play.libs.F.Promise.pure;
import static play.test.Helpers.*;

/**
 * Created by sowhat
 */
@RunWith(MockitoJUnitRunner.class)
public class GeolocationServiceTest extends WithApplication {

  @Mock
  private GeolocationCache cache;
  @Mock
  private GeolocationProvider provider;

  @Test
  public void testGetGeolocationRunWithNull() throws Exception {

  }

  public void runWithNull() {
    verify(cache, never());
    verify(provider, never());
    GeolocationService service = new GeolocationService(cache, provider);
    Promise<Geolocation> geolocationPromise = service.getGeolocation(null);
    geolocationPromise.onRedeem(Assert::assertNull);
  }

  public void runWithEmptyString() {
    verify(cache, never());
    verify(provider, never());
    GeolocationService service = new GeolocationService(cache, provider);
    Promise<Geolocation> geolocationPromise = service.getGeolocation("");
    geolocationPromise.onRedeem(Assert::assertNull);
  }

  @Test
  public void testGetGeolocationHappyPath() throws Exception {
    when(cache.get("192.30.252.129")).thenReturn(null);
    Geolocation geolocation = new Geolocation("192.30.252.129", "**");
    Promise<Geolocation> geolocationPromise = pure(geolocation);
    when(provider.get("192.30.252.129")).thenReturn(geolocationPromise);
    running(fakeApplication(), this::runHappyPath);
  }

  public void runHappyPath() {
    String ipAddress = "192.30.252.129";
    GeolocationService service = new GeolocationService(cache, provider);
    Promise<Geolocation> geolocationPromise = service.getGeolocation(ipAddress);
    Geolocation retrieved = geolocationPromise.get(5000);
    assertEquals(ipAddress, retrieved.getIp());
  }
}
