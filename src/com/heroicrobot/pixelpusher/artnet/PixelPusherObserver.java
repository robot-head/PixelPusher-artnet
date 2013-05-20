package com.heroicrobot.pixelpusher.artnet;

import java.util.Observable;
import java.util.Observer;

import com.heroicrobot.dropbit.registry.DeviceRegistry;

class PixelPusherObserver implements Observer {
  public boolean hasStrips = false;
  public ArtNetMapping mapping = new ArtNetMapping();

  public void update(Observable registry, Object updatedDevice) {
    // logging.info("Registry changed!");
    if (updatedDevice != null) {
      generateMapping((DeviceRegistry) registry);
      // println("Device change: " + updatedDevice);
    }
    this.hasStrips = true;
  }

  private void generateMapping(DeviceRegistry registry) {
    mapping.GenerateMapping(registry.getPushers(), true);
  }
}
