# play-geolocation-module

This is a play module for IP based geolocation.

Currently, the module is using the [freegeoip](http://freegeoip.net/) service to retrieve the IP data.

## Configuring

The first step is include the geolocation in your dependencies list, in `Build.scala` file:

```
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "geolocation-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "com.edulify" % "geolocation_2.10" % "1.0.2
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += Resolver.url("geolocation repository", url("http://blabluble.github.com/modules/releases/"))(Resolver.ivyStylePatterns)
  )

}

```

Don't forget to add the resolver to your list of resolvers, or it won't work!

### Caching

This module saves a cache of the data for the requested IPs. The default cache time to live is 3600 seconds (1 hour), but this time can be setted through the `conf` file, as show bellow.

Also, you can set cache off, also through the `conf` file. **Warning**: by setting off the cache, you can overload your system (and the service) due to multiple requests. Without cache activated, every call to `GeolocationService.getGeolocate` will necessarily make a request to the choosen web service.

### Configurations

In order to set the cache configurations, you must use the `application.conf` file, using the following keys:

```
geolocation {
  useCache  = true    # use cache (true | false)
  cacheTTL  = 3600    # time in seconds that cache will be kept
}
```

You can also set the key `geolocation.debug` with a boolean value (true | false) in order to enable / disable the debug of requests. This debug will use the method `play.Logger.debug` to show in the *Play terminal* the requests made to the service and their responses.

## Using

To use this module, its enough to import it in your class and use the static method `GeolocationService.getGeolocation`. This call will return an object of the class `Geolocation`, and can throw an Exception of the class `InvalidAddressException` when the argument address is not valid.

The `Geolocation` class has the following methods:

##### getIp()
Returns the ip returned by the web service.

##### getCountryCode()
Returns the country code (with two letters) returned by the web service.

##### getCountryName()
Returns the country name returned by the web service.

##### getRegionCode()
Returns the region code returned by the web service.

##### getRegionName()
Returns the region name returned by the web service.

##### getCity()
Returns the city returned by the web service.

##### getLatitude()
Returns the latitude returned by the web service.

##### getLongitude()
Returns the longitude returned by the web service.

----

Example code:

```java
import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;
import com.edulify.modules.geolocation.InvalidAddressException;

public class Application {
  public static Result index() {
    ...
    try {
      Geolocation geolocation = GeolocationService.getGeolocation(request.remoteAddress());
      if (geolocation == null) { // the service does not responded properly
        return null;
      }
      if ("US".equals(geolocation.getCountryCode())) {
        ...
      }
    } catch (InvalidAddressException ex) {
      ...
    }
    ...
  }
}
```