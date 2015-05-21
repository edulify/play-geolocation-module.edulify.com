package com.edulify.modules.geolocation;

import org.junit.Test;
import play.api.test.FakeApplication;
import play.test.WithApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static play.test.Helpers.*;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sovaalexandr
 */
public class GeolocationPluginTest extends WithApplication {

  @Test
  public void testOnStart() throws Exception {
    Map<String, Object> config = new HashMap<>(0);
    List<String> plugins = new ArrayList<>(1);
    plugins.add(GeolocationPlugin.class.getCanonicalName());
    running(fakeApplication(config, plugins), new Runnable() {
      @Override
      public void run() {
        assertThat(app.getWrappedApplication(), instanceOf(FakeApplication.class));
      }
    });
  }
}
