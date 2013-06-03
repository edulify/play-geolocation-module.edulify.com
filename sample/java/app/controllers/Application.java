package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import utils.GeolocationHelper;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.InvalidAddressException;

public class Application extends Controller {

    public static Result index() {
      return ok(index.render("Your new application is ready."));
    }

    public static Result getCountry(String addr) {
      if (GeolocationHelper.isReserved(addr)) {
        return ok(index.render("This ip is reserved."));
      }
      try {
        String country = GeolocationHelper.getCountry(addr);
        String message = country != null ? String.format("This ip comes from %s", country) :
                                           "Sorry, we couldn't connect to the webservice";
        return ok(index.render(message));
      } catch (InvalidAddressException ex) {
        return ok("Invalid ip");
      }
    }

    public static Result ipGeolocation(String addr) {
      try {

        Geolocation geolocation = GeolocationHelper.getGeolocation(addr);
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
                                 geolocation.getLongitude()));
      } catch (InvalidAddressException ex) {
        return ok(index.render("Invalid ip"));
      }
    }

}
