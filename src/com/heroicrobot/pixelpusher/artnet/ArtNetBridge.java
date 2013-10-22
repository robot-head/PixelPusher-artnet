package com.heroicrobot.pixelpusher.artnet;

import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class ArtNetBridge {

  private static PixelPusherObserver observer;
  static DeviceRegistry registry;
  boolean hasStrips;
  static ArtNetReceiver artnetReceiver;
  static SacnReceiver sacnReceiver;
  /**
   * @param args
   */
  public static void main(String[] args) {
    observer = new PixelPusherObserver();
    registry = new DeviceRegistry();
    registry.addObserver(observer);
    artnetReceiver = new ArtNetReceiver(observer);
    sacnReceiver = new SacnReceiver(observer);
    
    artnetReceiver.start();
    sacnReceiver.start();
    
    registry.startPushing();
    registry.setAutoThrottle(true);

  }
}
