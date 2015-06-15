package com.edulify.modules.geolocation;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class GeolocationFactoryTest {

  private GeolocationFactory factory = new GeolocationFactory();

  @Test
  public void testCreateEmpty() throws Exception {
    Geolocation geolocation = factory.create();
    assertThat(geolocation, instanceOf(Geolocation.class));
  }

  @Test
  public void testCreatePair() throws Exception {
    Geolocation geolocation = factory.create("192.30.252.129", "US");
    assertThat(geolocation, instanceOf(Geolocation.class));
  }

  @Test
  public void testCreate() throws Exception {
    Geolocation geolocation = factory.create("192.30.252.129", "US", "United States", "CA", "California", "San Francisco");
    assertThat(geolocation, instanceOf(Geolocation.class));
  }

  @Test
  public void testCreateComplete() throws Exception {
    Geolocation geolocation = factory.create("192.30.252.129", "US", "United States", "CA", "California", "San Francisco", 37.77, -122.394, "America/Los_Angeles");
    assertThat(geolocation, instanceOf(Geolocation.class));
  }
}
