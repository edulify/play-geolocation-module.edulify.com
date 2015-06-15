package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.Configuration;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import static org.junit.Assert.assertEquals;
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
  private WSRequest requestHolder;
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

    doRunTest(new Geolocation("192.30.252.129", "US"));
  }

  @Test
  public void testGetNoContent() throws Exception {
    when(response.getBody()).thenReturn("(null),IP_NOT_FOUND");
    when(response.getStatus()).thenReturn(Http.Status.OK);

    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("https://geoip.maxmind.com/a?l=someLicenseKey&i=192.30.252.129")).thenReturn(requestHolder);

    doRunTest(new Geolocation());
  }

  @Test
  public void testGetBadCode() throws Exception {
    when(response.getStatus()).thenReturn(Http.Status.SERVICE_UNAVAILABLE);
    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("https://geoip.maxmind.com/a?l=someLicenseKey&i=192.30.252.129")).thenReturn(requestHolder);

    doRunTest(new Geolocation());

    verify(response, never()).getBody();
  }

  private void doRunTest(Geolocation expected) {
    MaxmindProvider provider = new MaxmindProvider();
    provider.setClient(wsClient);
    provider.useConfiguration(new Configuration(ImmutableMap.of("geolocation.maxmind.license", "someLicenseKey")));
    Promise<Geolocation> geolocationPromise = provider.get("192.30.252.129");
    Geolocation retrieved = geolocationPromise.get(5000);
    assertEquals(expected, retrieved);
  }
}
