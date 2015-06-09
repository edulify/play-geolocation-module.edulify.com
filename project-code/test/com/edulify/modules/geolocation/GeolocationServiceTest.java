package com.edulify.modules.geolocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.libs.F.Promise;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static play.libs.F.Promise.pure;
/**
 * Created by sowhat
 */
@RunWith(MockitoJUnitRunner.class)
public class GeolocationServiceTest {

  @Mock
  private GeolocationCache cache;
  @Mock
  private GeolocationProvider provider;
  @Mock
  private Geolocation geolocation;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
  }

  @Test
  public void testGetGeolocationRunWithNull() throws Exception {
    GeolocationService service = new GeolocationService(cache, provider);
    Promise<Geolocation> geolocationPromise = service.getGeolocation(null);
    Geolocation retrieved = geolocationPromise.get(5000);
    assertNull(retrieved);
    verify(cache, never()).get(null);
    verify(cache, never()).set(null);
    verify(provider, never()).get(null);
  }

  @Test
  public void testGetGeolocationWithEmptyString() throws Exception {
    GeolocationService service = new GeolocationService(cache, provider);
    Promise<Geolocation> geolocationPromise = service.getGeolocation("");
    Geolocation retrieved = geolocationPromise.get(5000);
    assertNull(retrieved);
    verify(cache, never()).get("");
    verify(cache, never()).set(null);
    verify(provider, never()).get("");
  }

  @Test
  public void testGetGeolocationHappyPath() throws Exception {
    when(cache.get("192.30.252.129")).thenReturn(null);
    Promise<Geolocation> geolocationPromise = pure(geolocation);
    when(provider.get("192.30.252.129")).thenReturn(geolocationPromise);
    GeolocationService service = new GeolocationService(cache, provider);
    geolocationPromise = service.getGeolocation("192.30.252.129");
    Geolocation retrieved = geolocationPromise.get(5000);
    assertSame(geolocation, retrieved);
    verify(cache, times(1)).set(geolocation);
  }
}
