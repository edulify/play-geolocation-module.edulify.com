package controllers;

import play.*;
import play.mvc.*;
import play.libs.F;

import views.html.*;

import javax.inject.Inject;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;

public class Application extends Controller {

  private GeolocationService geolocationService;

  @Inject
  public Application(GeolocationService geolocationService) {
    this.geolocationService = geolocationService;
  }

  public Result index() {
    return ok(index.render("Your new application is ready."));
  }

  public F.Promise<Result> getCountry(final String addr) {
    return this.geolocationService.getGeolocation(addr)
        .map(new F.Function<Geolocation, Result>() {
          public Result apply(Geolocation geolocation) {
            String country = geolocation.getCountryName();
            String message = country != null ? String.format("This ip comes from %s", country) :
                "Sorry, we couldn't connect to the webservice";
            return ok(index.render(message));
          }
        });
  }

  public F.Promise<Result> getCountryCode(final String addr) {
    return this.geolocationService.getGeolocation(addr)
        .map(new F.Function<Geolocation, Result>() {
          public Result apply(Geolocation geolocation) {
            String countryCode = geolocation.getCountryCode();
            String message = countryCode != null ? String.format("This ip comes from %s", countryCode) :
                "Sorry, we couldn't connect to the webservice";
            return ok(index.render(message));
          }
        });
  }

  public F.Promise<Result> getGeolocation(final String addr) {
    return this.geolocationService.getGeolocation(addr)
        .map(new F.Function<Geolocation, Result>() {
          public Result apply(Geolocation geolocation) {
            String countryCode = geolocation.getCountryCode();
            String message = countryCode != null ? String.format("This ip comes from %s", countryCode) :
                "Sorry, we couldn't connect to the webservice";
            return ok(geoData.render(geolocation));
          }
        });
  }
}