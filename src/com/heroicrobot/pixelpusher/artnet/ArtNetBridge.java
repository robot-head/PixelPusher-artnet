package com.heroicrobot.pixelpusher.artnet;

import java.util.Observable;
import java.util.Observer;

import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class ArtNetBridge {

  private static PixelPusherObserver observer;
  static DeviceRegistry registry;
  boolean hasStrips;


  private static class PixelPusherObserver implements Observer {
    public boolean hasStrips = false;
    public void update(Observable registry, Object updatedDevice) {
      // logging.info("Registry changed!");
      if (updatedDevice != null) {
        // println("Device change: " + updatedDevice);
      }
      this.hasStrips = true;
    }
  };

  public static void PushArtnetBuffer() {
    // implement
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
    observer = new PixelPusherObserver();
    registry = new DeviceRegistry();
    registry.addObserver(observer);
    while (true) {
      if (observer.hasStrips) {
        PushArtnetBuffer();
      }
    }
  }

}
