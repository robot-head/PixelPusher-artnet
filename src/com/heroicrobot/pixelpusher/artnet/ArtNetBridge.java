package com.heroicrobot.pixelpusher.artnet;


import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class ArtNetBridge {

  private static PixelPusherObserver observer;
  static DeviceRegistry registry;
  boolean hasStrips;


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
