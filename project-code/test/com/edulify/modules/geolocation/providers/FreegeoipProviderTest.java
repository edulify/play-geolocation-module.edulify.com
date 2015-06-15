package com.edulify.modules.geolocation.providers;

import com.edulify.modules.geolocation.Geolocation;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static play.libs.F.Promise.pure;

@RunWith(MockitoJUnitRunner.class)
public class FreegeoipProviderTest {

  @Mock
  private WSClient wsClient;
  @Mock
  private WSRequest requestHolder;
  @Mock
  private WSResponse response;
  @Mock
  private Geolocation geolocation;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void testGetHappyPath() throws Exception {
    JsonNode contentJson;
    try {
      contentJson = Json.parse(new FileInputStream("test/resources/freegeoip.192.30.252.129.json"));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    when(response.getBody()).thenReturn(contentJson.asText());
    when(response.getStatus()).thenReturn(Http.Status.OK);
    when(response.asJson()).thenReturn(contentJson);
    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("http://freegeoip.net/json/192.30.252.129")).thenReturn(requestHolder);
    doRunTest(new Geolocation("192.30.252.129", "US", "United States", "CA", "California", "San Francisco", 37.77, -122.394, "America/Los_Angeles"));
  }

  private void doRunTest(Geolocation expected) {
    FreegeoipProvider provider = new FreegeoipProvider();
    provider.setClient(wsClient);
    Promise<Geolocation> geolocationPromise = provider.get("192.30.252.129");
    Geolocation retrieved = geolocationPromise.get(5000);
    assertNotNull(retrieved);
    assertEquals(expected, retrieved);
  }

  @Test
  public void testGetPartialJson() throws Exception {
    JsonNode contentJson;
    try {
      contentJson = Json.parse(new FileInputStream("test/resources/freegeoip.partial.json"));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    when(response.getBody()).thenReturn(contentJson.asText());
    when(response.getStatus()).thenReturn(Http.Status.OK);
    when(response.asJson()).thenReturn(contentJson);
    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("http://freegeoip.net/json/192.30.252.129")).thenReturn(requestHolder);

    doRunTest(new Geolocation());
  }

  @Test
  public void testGetNoContent() throws Exception {
    when(response.getBody()).thenReturn("not found");
    when(response.getStatus()).thenReturn(Http.Status.OK);
    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("http://freegeoip.net/json/192.30.252.129")).thenReturn(requestHolder);

    doRunTest(new Geolocation());
    verify(response, never()).asJson();
  }

  @Test
  public void testGetBadCode() throws Exception {
    when(response.getStatus()).thenReturn(Http.Status.SERVICE_UNAVAILABLE);
    Promise<WSResponse> responsePromise = pure(response);
    when(requestHolder.get()).thenReturn(responsePromise);
    when(wsClient.url("http://freegeoip.net/json/192.30.252.129")).thenReturn(requestHolder);

    doRunTest(new Geolocation());
    verify(response, never()).getBody();
    verify(response, never()).asJson();
  }
}
