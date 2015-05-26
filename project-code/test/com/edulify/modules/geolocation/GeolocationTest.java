package com.edulify.modules.geolocation;

import org.junit.Test;
import play.test.WithApplication;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

/**
 * Created by sowhat
 */
public class GeolocationTest {

  private static final double DELTA = .000001;
  private Geolocation geolocation = new Geolocation("192.30.252.129", "US", "United States", "CA", "California", "San Francisco", 37.77, -122.394, "America/Los_Angeles");

  @Test
  public void testGetIp() throws Exception {
    assertEquals("192.30.252.129", geolocation.getIp());
  }

  @Test
  public void testGetCountryCode() throws Exception {
    assertEquals("US", geolocation.getCountryCode());
  }

  @Test
  public void testGetCountryName() throws Exception {
    assertEquals("United States", geolocation.getCountryName());
  }

  @Test
  public void testGetRegionCode() throws Exception {
    assertEquals("CA", geolocation.getRegionCode());
  }

  @Test
  public void testGetRegionName() throws Exception {
    assertEquals("California", geolocation.getRegionName());
  }

  @Test
  public void testGetCity() throws Exception {
    assertEquals("San Francisco", geolocation.getCity());
  }

  @Test
  public void testGetLatitude() throws Exception {
    assertEquals(37.77, geolocation.getLatitude(), DELTA);
  }

  @Test
  public void testGetLongitude() throws Exception {
    assertEquals(-122.394, geolocation.getLongitude(), DELTA);
  }

  @Test
  public void testGetTimeZone() throws Exception {
    assertEquals("America/Los_Angeles", geolocation.getTimeZone());
  }

  @Test
  public void testToString() throws Exception {
    String representation = geolocation.toString();
    assertThat(representation, containsString(Geolocation.class.getCanonicalName()));
    assertThat(representation, containsString("[ip=192.30.252.129,countryCode=US,countryName=United States,regionCode=CA,regionName=California,city=San Francisco,latitude=37.77,longitude=-122.394,timeZone=America/Los_Angeles]"));
  }
}