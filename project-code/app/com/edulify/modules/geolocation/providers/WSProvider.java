package com.edulify.modules.geolocation.providers;

import play.libs.ws.WSClient;

/**
 * Created by sowhat
 */
public interface WSProvider {
  void setClient(WSClient client);
}
