package com.edulify.modules.geolocation;

import org.junit.Test;
import play.test.Helpers;

import static org.fest.assertions.Assertions.assertThat;

public class ConfigTest {

  @Test
  public void should_return_a_boolean_configuration_when_it_exists() {
    Helpers.running(Helpers.fakeApplication(), new Runnable() {
      @Override
      public void run() {
        boolean value = Config.getBooleanOr("geolocation.cache.on", false);
        assertThat(value).isTrue();
      }
    });
  }

  @Test
  public void should_return_a_boolean_alternative_when_configuration_key_do_not_exists() {
    Helpers.running(Helpers.fakeApplication(), new Runnable() {
      @Override
      public void run() {
        boolean value = Config.getBooleanOr("geolocation.cache.off", false);
        assertThat(value).isFalse();
      }
    });
  }

  @Test
  public void should_return_the_milliseconds_value_when_configuration_exists() {
    Helpers.running(Helpers.fakeApplication(), new Runnable() {
      @Override
      public void run() {
        long value = Config.getMillisecondsOr("geolocation.cache.ttl", 5000);
        assertThat(value).isEqualTo(100l);
      }
    });
  }

  @Test
  public void should_return_the_altenative_milliseconds_value_when_configuration_key_do_not_exists() {
    Helpers.running(Helpers.fakeApplication(), new Runnable() {
      @Override
      public void run() {
        long value = Config.getMillisecondsOr("geolocation.cache.tttttt", 5000);
        assertThat(value).isEqualTo(5000);
      }
    });
  }
}
