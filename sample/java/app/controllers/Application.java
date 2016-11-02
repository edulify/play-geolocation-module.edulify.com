package controllers;

import com.edulify.modules.geolocation.Geolocation;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;

import com.edulify.modules.geolocation.GeolocationService;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class Application extends Controller {

  private GeolocationService geolocationService;

  @Inject
  public Application(GeolocationService geolocationService) {
    this.geolocationService = geolocationService;
  }

  public Result index() {
    return ok(index.render("Your new application is ready."));
  }

  public CompletionStage<Result> getCountry(final String addr) {
    return geolocationService.getGeolocation(addr)
                             .thenApplyAsync(geolocation -> ok(index.render(formHeader(geolocation, addr, Geolocation::getCountryName))));
  }

  public CompletionStage<Result> getCountryCode(final String addr) {
    return geolocationService.getGeolocation(addr)
        .thenApplyAsync(geolocation -> ok(index.render(formHeader(geolocation, addr))));
  }

  public CompletionStage<Result> getGeolocation(final String addr) {
    return geolocationService.getGeolocation(addr)
        .thenApplyAsync(geolocation -> ok(geoData.render(geolocation, formHeader(geolocation, addr))));
  }

  private String formHeader(Geolocation geolocation, String addr)
  {
    return formHeader(geolocation, addr, Geolocation::getCountryCode);
  }

  private String formHeader(Geolocation geolocation, String addr, Function<Geolocation, String> propertyAccessor)
  {
    return ofNullable(geolocation).flatMap(location -> ofNullable(propertyAccessor.apply(location)))
                           .map(countryCode -> format("This ip comes from %s", countryCode))
                           .orElseGet(() -> "Sorry, no data for ip: " + addr);
  }
}