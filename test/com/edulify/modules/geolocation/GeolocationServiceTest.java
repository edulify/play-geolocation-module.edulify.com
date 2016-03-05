package com.edulify.modules.geolocation;

import org.junit.*;
import org.hamcrest.*;
import org.mockito.Mockito;

import play.*;
import play.test.WithApplication;
import play.inject.guice.GuiceApplicationBuilder;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.*;
import static play.inject.Bindings.bind;

public class GeolocationServiceTest extends WithApplication {

  private final String ipAddress = "192.30.252.129";
  private final String countryCode = "BR";

  @Override
  public Application provideApplication() {
    Geolocation geolocation = new Geolocation(ipAddress, countryCode);

    GeolocationProvider provider = Mockito.mock(GeolocationProvider.class);
    Mockito.when(provider.get(ipAddress)).thenReturn(CompletableFuture.completedFuture(geolocation));

    return new GuiceApplicationBuilder()
      .in(new File("."))
      .in(Mode.TEST)
      .configure("geolocation.cache.on", true)
      .bindings(bind(GeolocationProvider.class).toInstance(provider))
      .build();
  }

  @Test
  public void shouldGetAGeolocationForAGivenIp() throws Exception {
    GeolocationService service = app.injector().instanceOf(GeolocationService.class);
    Geolocation geolocation = service.getGeolocation(ipAddress).toCompletableFuture().get(1, TimeUnit.SECONDS);

    Assert.assertThat(geolocation.getIp(), CoreMatchers.equalTo(ipAddress));
    Assert.assertThat(geolocation.getCountryCode(), CoreMatchers.equalTo(countryCode));
  }

  @Test
  public void shouldSetAGeolocationCache() throws InterruptedException {
    GeolocationService service = app.injector().instanceOf(GeolocationService.class);
    GeolocationCache cache = app.injector().instanceOf(GeolocationCache.class);

    CompletionStage<Geolocation> promise = service.getGeolocation(ipAddress);

    await().atMost(5, TimeUnit.SECONDS).until(() -> {
      return promise.toCompletableFuture().isDone();
    });

    Geolocation geolocation = cache.get(ipAddress);
    Assert.assertThat(geolocation, CoreMatchers.notNullValue());
  }
}
