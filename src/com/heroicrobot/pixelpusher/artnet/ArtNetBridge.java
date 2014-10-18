package com.heroicrobot.pixelpusher.artnet;

import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class ArtNetBridge {

  public static ColourOrdering order;
  public static boolean packing=true;
	
  private static PixelPusherObserver observer;
  static DeviceRegistry registry;
  boolean hasStrips;
  static ArtNetReceiver artnetReceiver;
  static SacnReceiver sacnReceiver;
  static boolean debug=false;
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
	
	if (args.length > 1) {
		if (args[1].toLowerCase().startsWith("nopack")) {
			packing = false;
		}
	}
	
	if (args.length > 2) {
		if (args[2].toLowerCase().startsWith("debug")) {
			debug = true;
		}
	}
	
	System.out.println("Red components at channel + "+ order.getOffset(ColourOrdering.RED));
	System.out.println("Green components at channel + "+ order.getOffset(ColourOrdering.GREEN));
	System.out.println("Blue components at channel + "+ order.getOffset(ColourOrdering.BLUE));
	
	if (packing) {
		System.out.println("Universe packing mode pack: universes will be filled");
	} else {
		System.out.println("Universe packing mode nopack: universes will be left part-filled");
	}
	if (debug) {
		System.out.println("Debug mode: a message will be printed every 100 Art-Net packets.");
	} else {
		System.out.println("Debug mode disabled: no message will be printed for Art-Net packets.");
	}
	  
    observer = new PixelPusherObserver();
    registry = new DeviceRegistry();
    registry.addObserver(observer);
    artnetReceiver = new ArtNetReceiver(observer, debug);
    sacnReceiver = new SacnReceiver(observer);
    
    artnetReceiver.start();
    sacnReceiver.start();
    
    registry.startPushing();
    registry.setAutoThrottle(true);

  }
}
