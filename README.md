# Play Geolocation Plugin

[![Build Status](https://travis-ci.org/edulify/play-geolocation-module.edulify.com.svg)](https://travis-ci.org/edulify/play-geolocation-module.edulify.com)

This is a play module for IP based geolocation. Currently, the module supports use of one of the following service to retrieve the IP data:

#### [Freegeoip](http://freegeoip.net/)

> freegeoip.net provides a public HTTP API for software developers to search the geolocation of IP addresses. It uses a database of IP addresses that are associated to cities along with other relevant information like time zone, latitude and longitude.

#### [Maxmind GeoIP2 Country](https://www.maxmind.com/en/country)

> Determine an Internet visitor's country based on their IP address.

However, support to other geolocation services is possible using the API provided by this plugin.

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
    "com.edulify" %% "geolocation" % "2.0.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
  )

}

```

#### Setup your application DI container:

If you using `Spring IoC` for DI configuration could be following

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd">
  <bean id="wsClient" class="play.libs.ws.WS" factory-method="client"/>
  <bean id="geolocationFactory" class="com.edulify.modules.geolocation.GeolocationFactory"/>
  <bean id="geolocationProvider" class="com.edulify.modules.geolocation.providers.FreegeoipProvider">
    <constructor-arg ref="wsClient"/>
    <constructor-arg ref="geolocationFactory"/>
  </bean>
  <bean id="geolocationCache" class="com.edulify.modules.geolocation.GeolocationCache"/>
  <bean id="geolocationService" class="com.edulify.modules.geolocation.GeolocationService">
    <constructor-arg ref="geolocationCache"/>
    <constructor-arg ref="geolocationProvider"/>
  </bean>
  <bean id="applicationController" class="controllers.Application">
    <constructor-arg ref="geolocationService"/>
  </bean>
</beans>
```

Than [configure project to use DI](https://www.playframework.com/documentation/2.3.x/JavaInjection).

## Configurations

This plugins offers the following configurations:

| Configuration           | Description                             | Default           |
|:------------------------|:----------------------------------------|:------------------|
| `geolocation.cache.on`  | Caches geolocation results calls        | `false`           |
| `geolocation.cache.ttl` | How long it should cache the results    | none              |
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

Also, notice that the cache uses the cache support offered by Playframework.


## Code example

Right now there is support to both async and sync geolocation calls, both using [the Play WS API](https://playframework.com/documentation/2.3.x/JavaWS). 

### Async Example Code

This is the expected way to use the plugin.

```java
import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;

public class Application {
  private final GeolocationService geolocationService;

  public Application(GeolocationService geolocationService) {
    this.geolocationService = geolocationService;
  }
  public Result index() {
    ...
    Promise<Geolocation> promise = geolocationService.getGeolocation(request.remoteAddress());
    return promise.map(new Function<Geolocation, Result>() {
      ...
    });
  }
}
```

### Sync Example code (DEPRECATED):

This will be removed in a future release and it is exists just to keep compatibility.

```java
import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;

public class Application {
  private final GeolocationService geolocationService;

  public Application(GeolocationService geolocationService) {
    this.geolocationService = geolocationService;
  }
  public static Result index() {
    ...
    Geolocation geolocation = geolocationService.getGeolocation(request.remoteAddress()).get(5000);
    return ok(viewGeolocation.render(geolocation));
  }
}
```

## Implement your own geolocation service provider

Out of the box, this plugins supports just Maxmind and Freegeoip, but you can add your own geolocation service provider implementation and everything will works as expected. To do that, just create an implementation of `com.edulify.modules.geolocation.GeolocationProvider` and then configure it:

```java
package com.acme.geolocation;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationProvider;
import play.libs.F;

public class MyGeolocationProvider implements GeolocationProvider {

    @Override
    public F.Promise<Geolocation> get(String ip) {
        // Do a request to your geolocation service and 
        // then return a Promise with a geolocation object
    }
}
```

After that, you just have to configure the provider for DI container

```xml
    <bean id="geolocationProvider" class="com.acme.geolocation.MyGeolocationProvider" />
```

Things like cache and timeouts will work seamless.

## More plugins from [Edulify.com](https://edulify.com)

1. [Sitemap Plugin](https://github.com/edulify/play-sitemap-module.edulify.com)
2. [HikariCP Plugin](https://github.com/edulify/play-hikaricp.edulify.com)
3. [Currency Plugin](https://github.com/edulify/play-currency-converter-module.edulify.com)