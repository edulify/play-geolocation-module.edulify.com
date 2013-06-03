package test;

import com.edulify.modules.geolocation.Geolocation;
import com.edulify.modules.geolocation.GeolocationService;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.cache.Cache;
import play.test.Helpers;
import play.test.FakeApplication;

import utils.GeolocationHelper;

public class GeolocationHelperTest {

  protected static FakeApplication app;

  @BeforeClass
  public static void startApp() {
    app = Helpers.fakeApplication();
    Helpers.start(app);
  }

  @AfterClass
  public static void stopApp() {
    Helpers.stop(app);
    System.out.println();
  }

  @Test
  public void should_return_that_localhost_is_a_reserved_ip() {
    Assertions.assertThat(GeolocationHelper.isReserved("localhost")).isTrue();
  }

  @Test
  public void should_return_that_0_0_0_0_is_a_reserved_ip() {
    Assertions.assertThat(GeolocationHelper.isReserved("0.0.0.0")).isTrue();
  }

  @Test
  public void should_return_that_255_255_255_255_is_a_reserved_ip() {
    Assertions.assertThat(GeolocationHelper.isReserved("255.255.255.255")).isTrue();
  }

  @Test
  public void should_return_that_brazil_government_site_is_from_brazil() {
    Assertions.assertThat(GeolocationHelper.getCountry("www.brasil.gov.br").equals("Brazil")).isTrue();
    Assertions.assertThat(GeolocationHelper.getCountryCode("www.brasil.gov.br").equals("BR")).isTrue();
  }

  @Test
  public void should_return_that_usa_government_site_is_from_us() {
    Assertions.assertThat(GeolocationHelper.getCountry("www.usa.gov").equals("United States")).isTrue();
    Assertions.assertThat(GeolocationHelper.getCountryCode("www.usa.gov").equals("US")).isTrue();
  }

  @Test
  public void should_get_geolocation_data_from_play_cache_if_available() {
    Geolocation geo = new Geolocation("ip", "cc", "cn", "rc", "rn", "ct", 10.0, 20.0);
    Cache.set("geolocation-cache-1.2.3.4", geo);
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4")).isEqualTo(geo);
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4").getIp()).isEqualTo("ip");
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4").getCountryCode()).isEqualTo("cc");
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4").getCountryName()).isEqualTo("cn");
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4").getRegionCode()).isEqualTo("rc");
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4").getRegionName()).isEqualTo("rn");
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4").getCity()).isEqualTo("ct");
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4").getLatitude()).isEqualTo(10.0);
    Assertions.assertThat(GeolocationHelper.getGeolocation("1.2.3.4").getLongitude()).isEqualTo(20.0);
  }

}