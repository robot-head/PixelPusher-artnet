package com.heroicrobot.pixelpusher.artnet;

import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class ArtNetBridge {

  private static PixelPusherObserver observer;
  static DeviceRegistry registry;
  boolean hasStrips;

  /**
   * @param args
   */
  public static void main(String[] args) {
    observer = new PixelPusherObserver();
    registry = new DeviceRegistry();
    registry.addObserver(observer);
    ArtNetReceiver receiver = new ArtNetReceiver(observer);
    receiver.start();
    registry.startPushing();
    registry.setAutoThrottle(true);

  }
}
