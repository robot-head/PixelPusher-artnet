package com.heroicrobot.pixelpusher.artnet;

import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class ArtNetBridge {

  public static ColourOrdering order;	
	
  private static PixelPusherObserver observer;
  static DeviceRegistry registry;
  boolean hasStrips;
  static ArtNetReceiver artnetReceiver;
  static SacnReceiver sacnReceiver;
  /**
   * @param args
   */
  public static void main(String[] args) {
	  // parse commandline argument-  colour ordering.
	if (args.length > 0) {
		System.out.println("Setting colour ordering to "+args[0]);
		order = new ColourOrdering(args[0]);
	} else {
		System.out.println("Using default colour ordering.");
		order = new ColourOrdering(ColourOrdering.RGB);
	}
	
	System.out.println("Red components at channel + "+ order.getOffset(ColourOrdering.RED));
	System.out.println("Green components at channel + "+ order.getOffset(ColourOrdering.GREEN));
	System.out.println("Blue components at channel + "+ order.getOffset(ColourOrdering.BLUE));
	  
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
