package com.edulify.modules.geolocation;

import play.Play;

public class Config {

  public static boolean getBooleanOr(String key, Boolean alternative) {
    return Play.application().configuration().getBoolean(key, alternative);
  }

  public static long getMillisecondsOr(String key, long alternative) {
    return Play.application().configuration().getMilliseconds(key, alternative);
  }

  public static String getStringOr(String key, String alternative) {
    return Play.application().configuration().getString(key, alternative);
  }

  public static String getString(String key) {
    return Play.application().configuration().getString(key);
  }
}