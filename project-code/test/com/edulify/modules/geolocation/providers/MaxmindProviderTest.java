package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationFactory;
import com.edulify.modules.geolocation.GeolocationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static play.libs.F.Promise.pure;

@RunWith(MockitoJUnitRunner.class)
public class MaxmindProviderTest {

  @Mock
  private WSClient wsClient;
  @Mock
  private GeolocationFactory factory;
  @Mock
  private WSRequestHolder requestHolder;
  @Mock
  private WSResponse response;
  @Mock
  private Geolocation geolocation;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
  }

  @Test
  public void testGetHappyPath() throws Exception {
    when(response.getBody()).thenReturn("US");
    when(response.getStatus()).thenReturn(Http.Status.OK);

    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("https://geoip.maxmind.com/a?l=someLicenseKey&i=192.30.252.129")).thenReturn(requestHolder);
    when(factory.create("192.30.252.129", "US")).thenReturn(geolocation);

    doRunTest();

    verify(factory, never()).create();
  }

  @Test
  public void testGetNoContent() throws Exception {
    when(response.getBody()).thenReturn("(null),IP_NOT_FOUND");
    when(response.getStatus()).thenReturn(Http.Status.OK);

    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("https://geoip.maxmind.com/a?l=someLicenseKey&i=192.30.252.129")).thenReturn(requestHolder);
    when(factory.create()).thenReturn(geolocation);

    doRunTest();

    verify(factory, never()).create("192.30.252.129", "US");
  }

  @Test
  public void testGetBadCode() throws Exception {
    verify(response, never()).getBody();
    when(response.getStatus()).thenReturn(Http.Status.SERVICE_UNAVAILABLE);
    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("https://geoip.maxmind.com/a?l=someLicenseKey&i=192.30.252.129")).thenReturn(requestHolder);
    when(factory.create()).thenReturn(geolocation);
    verify(factory, never()).create("192.30.252.129", "US");

    doRunTest();
  }

  private void doRunTest() {
    GeolocationProvider provider = new MaxmindProvider(wsClient, factory, "someLicenseKey");
    Promise<Geolocation> geolocationPromise = provider.get("192.30.252.129");
    Geolocation retrieved = geolocationPromise.get(5000);
    assertSame(geolocation, retrieved);
  }
}
