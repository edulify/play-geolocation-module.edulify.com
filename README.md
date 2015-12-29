# Play Geolocation Plugin

[![Build Status](https://travis-ci.org/edulify/play-geolocation-module.edulify.com.svg)](https://travis-ci.org/edulify/play-geolocation-module.edulify.com)

This is a play module for IP based [Geolocation](https://en.wikipedia.org/wiki/Geolocation). Currently, the module supports the following services to retrieve the IP data:

#### [Freegeoip](http://freegeoip.net/)

> freegeoip.net provides a public HTTP API for software developers to search the geolocation of IP addresses. It uses a database of IP addresses that are associated to cities along with other relevant information like time zone, latitude and longitude.

#### [Maxmind GeoIP2 Country](https://www.maxmind.com/en/country)

> Determine an Internet visitor's country based on their IP address.

However, support to other geolocation services is possible using the API provided by this plugin.

## Compatibility Matrix

| Playframework version | Module version |
|:----------------------|:---------------|
| 2.4.x                 | 2.0.0          |
| 2.3.x                 | 1.4.1          |
| 2.3.x                 | 1.4.0          |
| 2.3.x                 | 1.3.0          |
| 2.3.x                 | 1.2.0          |

## How to use

The first step is include the the dependency in your `build.sbt` or `project/Build.scala` file:

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
  "com.edulify" %% "geolocation" % "2.0.0"
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
    "com.edulify" %% "geolocation" % "2.0.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
  )

}
```

#### Enable the module in your `conf/application.conf`:

Since there is support to Freegeoip and Maxmind, there is also two modules that you enable, depending on which one you want to use. To eanble the module, just add the following line to you `conf/application.conf` file:

```
play.modules.enabled += "com.edulify.modules.geolocation.providers.FreegeoipModule"
```

Or, in case you want to use Maxmind instead:

```
play.modules.enabled += "com.edulify.modules.geolocation.providers.MaxmindModule"
```

## Configurations

This plugins offers the following configurations:

| Configuration           | Description                             | Default           |
|:------------------------|:----------------------------------------|:------------------|
| `geolocation.cache.on`  | Caches geolocation results calls        | `false`           |
| `geolocation.cache.ttl` | How long it should cache the results    | 5 seconds         |
| `geolocation.enabled`   | If the plugin is enabled or not         | `true`            |
| `geolocation.maxmind.license` | Maxmind license                   | none              |

Per instance, you can add the following in your `conf/application.conf`:

```
geolocation {
  cache {
    on = true
    ttl = 10s
  }
  maxmind.license = "your-maxmind-license"
}
```

Also, notice that the cache uses the cache support offered by Playframework. A complete configuration can be found below:

```
play.modules.enabled += "com.edulify.modules.geolocation.providers.FreegeoipModule"

geolocation {
  cache {
    on = true
    ttl = 10s
  }
}
```

## Code example

This is the expected way to use the plugin.

```java
import javax.inject.Inject;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;

public class Application {

  private GeolocationService geolocationService;

  @Inject
  public Application(GeolocationService geolocationService) {
    this.geolocationService = geolocationService;
  }

  public static Result index() {
    ...
    Promise<Geolocation> promise = geolocationService.getGeolocation(request.remoteAddress());
    return promise.map(new Function<Geolocation, Result>() {
      ...
    });
  }
}
```

## Implement your own geolocation service provider

Out of the box, this plugins supports Freegeoip and Maxmind, but you can add your own geolocation service provider implementation and everything will works as expected. To do that, just create an implementation of `com.edulify.modules.geolocation.GeolocationProvider` and a [Play Module](https://www.playframework.com/documentation/2.4.x/Modules) to configure it:

#### The `GeolocationProvider`:

```java
package com.acme.geolocation;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationProvider;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

public class MyGeolocationProvider implements GeolocationProvider {

    private WSClient ws;

    @Inject
    public MyGeolocationProvider(WSClient ws) {
        this.ws = ws;
    }

    @Override
    public F.Promise<Geolocation> get(String ip) {
        // Do a request to your geolocation service and
        // then return a Promise with a geolocation object
    }
}
```

#### The Module:

```java
package com.acme.geolocation;

import com.edulify.modules.geolocation.GeolocationProvider;
import com.google.inject.AbstractModule;

public class MyGeolocationModule extends AbstractModule {

  @Override
  protected void configure() {
    // bind to your own MyGeolocationProvider implementation
    bind(GeolocationProvider.class).to(MyGeolocationProvider.class);
  }
}

```

After that, you just have to configure the provider:

```
# Configure your own module here
play.modules.enabled += "com.acme.geolocation.MyGeolocationModule"

geolocation {
  cache {
    on = true
    ttl = 10s
  }
}
```

Things like cache and timeouts will work seamless.

## More plugins from [Edulify.com](https://edulify.com)

1. [Sitemap Plugin](https://github.com/edulify/play-sitemap-module.edulify.com)
2. [HikariCP Plugin](https://github.com/edulify/play-hikaricp.edulify.com)
3. [Currency Plugin](https://github.com/edulify/play-currency-converter-module.edulify.com)

## License

Copyright 2014 Edulify.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.