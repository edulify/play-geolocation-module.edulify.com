# play-geolocation-module

[![Build Status](https://travis-ci.org/edulify/play-geolocation-module.edulify.com.svg)](https://travis-ci.org/edulify/play-geolocation-module.edulify.com)

This is a play module for IP based geolocation.

Currently, the module supports use of one of the following service to retrieve the IP data:
* [Freegeoip](http://freegeoip.net/) - Free web service for retrieve IP data
* [Maxind GeoIP Country Web Service](https://www.maxmind.com/en/country) - Web service that retrieve the country of the given IP, with reliability of 99.98%

## Configuring

The first step is include the geolocation in your dependencies list, in your `build.sbt` or `project/Build.scala` file:

#### `build.sbt`

```scala
name := "sitemapper-java-sample"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  javaCore,
  javaJdbc,
  javaEbean,
  "com.edulify" %% "geolocation" % "1.3.0"
)

resolvers ++= Seq(
  Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
)
```

#### `project/Build.scala`

```scala
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "geolocation-java-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "com.edulify" %% "geolocation" % "1.3.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
  )

}

```

Don't forget to add the resolver to your list of resolvers, or it won't work!

### Configurations

In order to set the cache configurations, you must use the `application.conf` file, using the following keys:

```
geolocation {
  useCache  = true                             # use cache (true | false)
  cacheTTL  = 3600                             # time in seconds that cache will be kept
  source    = FREEGEOIP                        # the web service that should be used for
                                               # your IP queries (FREEGEOIP | GEOIP_COUNTRY)
  # maxmind_license = yourMaxmindLicenseCode   # used only when the source is GEOIP_COUNTRY
}
```

You can also set the key `geolocation.debug` with a boolean value (true | false) in order to enable / disable the debug of requests. This debug will use the method `play.Logger.debug` to show in the *Play terminal* the requests made to the service and their responses.

### Caching

This module saves a cache of the data for the requested IPs. The default cache time to live is 3600 seconds (1 hour), but this time can be setted through the `conf` file, as shown above.

Also, you can set cache off, also through the `conf` file. **Warning**: by setting off the cache, you can overload your system (and the service) due to multiple requests. Also, if you are using the Maxmind GeoIP service, cache off can waste your request quota. Without cache activated, every call to `GeolocationService.getGeolocate` will necessarily make a request to the choosen web service.


## Using

To use this module, its enough to import it in your class and use the static method `GeolocationService.getGeolocation`. This call will return an object of the class `Geolocation`, and can throw the following Exceptions:
* `InvalidAddressException` - when the argument address is not valid.
* `ServiceErrorException` - when the Web service response is not recognized.

The `Geolocation` class has the following methods:

##### *String* getIp()
Returns the ip returned by the web service.

##### *String* getCountryCode()
Returns the country code (with two letters) returned by the web service.

##### *String* getCountryName()
Returns the country name returned by the web service.

##### *String* getRegionCode()
Returns the region code returned by the web service.

##### *String* getRegionName()
Returns the region name returned by the web service.

##### *String* getCity()
Returns the city returned by the web service.

##### *double* getLatitude()
Returns the latitude returned by the web service.

##### *double* getLongitude()
Returns the longitude returned by the web service.

----

Note that all methods but `getIp()` and `getCountryCode()` will return `null` or `0` value when the source is *GEOIP_COUNTRY*.

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