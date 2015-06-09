package controllers;

import com.google.inject.Inject;
import play.mvc.*;
import play.libs.F;

import views.html.*;


import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;

public class Application extends Controller {

  private final GeolocationService geolocationService;

  @Inject
  public Application(GeolocationService geolocationService) {
    this.geolocationService = geolocationService;
  }

  public Result index() {
    return ok(index.render("Your new application is ready."));
  }

  private Result formResultForPlainGeolocation(final String addr, final Geolocation geolocation) {
    if (geolocation == null) {
      return ok(index.render("Sorry, we couldn't connect to the webservice"));
    }
    return ok(geoData.render(addr,
        geolocation.getIp(),
        geolocation.getCountryCode(),
        geolocation.getCountryName(),
        geolocation.getRegionCode(),
        geolocation.getRegionName(),
        geolocation.getCity(),
        geolocation.getLatitude(),
        geolocation.getLongitude(),
        geolocation.getTimeZone()));
  }

  public F.Promise<Result> asyncCountry(final String addr) {
    return geolocationService.getGeolocation(addr)
        .map(geolocation -> {
          String country = geolocation.getCountryName();
          String message = country != null ? String.format("This ip comes from %s", country) :
              "Sorry, we couldn't connect to the webservice";
          return ok(index.render(message));
        });
  }

  public F.Promise<Result> asyncCountryCode(final String addr) {
    return geolocationService.getGeolocation(addr)
        .map(geolocation -> {
          String countryCode = geolocation.getCountryCode();
          String message = countryCode != null ? String.format("This ip comes from %s", countryCode) :
              "Sorry, we couldn't connect to the webservice";
          return ok(index.render(message));
        });
  }

  public F.Promise<Result> asyncGeolocation(final String addr) {
    return geolocationService.getGeolocation(addr)
        .map(geolocation -> formResultForPlainGeolocation(addr, geolocation));
  }
}