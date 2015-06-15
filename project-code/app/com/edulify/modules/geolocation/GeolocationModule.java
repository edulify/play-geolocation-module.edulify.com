package com.edulify.modules.geolocation;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import play.Configuration;
import play.Environment;

public class GeolocationModule extends AbstractModule {

    private final Environment environment;
    private final Configuration configuration;

    public GeolocationModule(Environment environment, Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        try {
            bind(GeolocationService.class).toConstructor(GeolocationService.class.getConstructor(GeolocationCache.class, GeolocationProvider.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }
    }

    @Provides
    private GeolocationCache provideGeolocationCache()
    {
        return new GeolocationCache(
                configuration.getBoolean("geolocation.cache.on"),
                configuration.getMilliseconds("geolocation.cache.ttl", 5000l)
        );
    }
}
