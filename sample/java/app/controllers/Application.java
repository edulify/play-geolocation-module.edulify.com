package controllers;

import play.*;
import play.mvc.*;
import play.libs.F;

import views.html.*;

import utils.GeolocationHelper;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;
import com.edulify.modules.geolocation.AsyncGeolocationService;

public class Application extends Controller {

  public static Result index() {
    return ok(index.render("Your new application is ready."));
  }

  public static Result syncCountry(String addr) {
    if (GeolocationHelper.isReserved(addr)) {
      return ok(index.render("This ip is reserved."));
    }

    Geolocation geolocation = GeolocationService.getGeolocation(addr);
    String country = geolocation.getCountryName();
    String message = country != null ? String.format("This ip comes from %s", country) :
        "Sorry, we couldn't connect to the webservice";
    return ok(index.render(message));
  }

  public static Result syncCountryCode(String addr) {
    if (GeolocationHelper.isReserved(addr)) {
      return ok(index.render("This ip is reserved."));
    }

    Geolocation geolocation = GeolocationService.getGeolocation(addr);
    String countryCode = geolocation.getCountryCode();
    String message = countryCode != null ? String.format("This ip comes from %s", countryCode) :
        "Sorry, we couldn't connect to the webservice";
    return ok(index.render(message));
  }

  public static Result syncGeolocation(String addr) {
    Geolocation geolocation = GeolocationService.getGeolocation(addr);
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

  public static F.Promise<Result> asyncCountry(final String addr) {
    return AsyncGeolocationService.getGeolocation(addr)
        .map(new F.Function<Geolocation, Result>() {
          public Result apply(Geolocation geolocation) {
            String country = geolocation.getCountryName();
            String message = country != null ? String.format("This ip comes from %s", country) :
                "Sorry, we couldn't connect to the webservice";
            return ok(index.render(message));
          }
        });
  }

  public static F.Promise<Result> asyncCountryCode(final String addr) {
    return AsyncGeolocationService.getGeolocation(addr)
        .map(new F.Function<Geolocation, Result>() {
          public Result apply(Geolocation geolocation) {
            String countryCode = geolocation.getCountryCode();
            String message = countryCode != null ? String.format("This ip comes from %s", countryCode) :
                "Sorry, we couldn't connect to the webservice";
            return ok(index.render(message));
          }
        });
  }

  public static F.Promise<Result> asyncGeolocation(final String addr) {
    return AsyncGeolocationService.getGeolocation(addr)
        .map(new F.Function<Geolocation, Result>() {
          public Result apply(Geolocation geolocation) {
            String countryCode = geolocation.getCountryCode();
            String message = countryCode != null ? String.format("This ip comes from %s", countryCode) :
                "Sorry, we couldn't connect to the webservice";
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
        });
  }
}