package com.edulify.modules.geolocation;

import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public final class GeolocationService {

  private GeolocationProvider provider;
  private GeolocationCache cache;

  @Inject
  public GeolocationService(GeolocationProvider provider, GeolocationCache cache) {
    this.provider = provider;
    this.cache = cache;
  }

  public CompletionStage<Geolocation> getGeolocation(String ip) {
    Geolocation geolocation = cache.get(ip);
    if (geolocation != null) return CompletableFuture.completedFuture(geolocation);

    CompletionStage<Geolocation> promise = provider.get(ip);
    promise.thenAcceptAsync(gl -> cache.set(gl), HttpExecution.defaultContext());
    return promise;
  }
}
