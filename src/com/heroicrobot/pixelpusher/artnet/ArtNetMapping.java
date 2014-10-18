package com.heroicrobot.pixelpusher.artnet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;


public class ArtNetMapping {

  public static final int CHANNELS_PER_UNIVERSE = 512;

  Map<ArtNetLocation, PixelPusherLocation> mapping;
  List<InetAddress> multicastAddresses;
  List<PixelPusher> mappedPushers;

  ArtNetMapping() {
    mapping = new HashMap<ArtNetLocation, PixelPusherLocation>();
    multicastAddresses = new CopyOnWriteArrayList<InetAddress>();
    mappedPushers = new CopyOnWriteArrayList<PixelPusher>();
  }

  public List<PixelPusher> getMappedPushers() {
	 return mappedPushers;
  }
  
  public PixelPusherLocation getPixelPusherLocation(int universe, int channel) {
    ArtNetLocation loc = new ArtNetLocation(universe, channel, getSacnMulticast(universe));
    return this.mapping.get(loc);
  }

  public void GenerateMapping(List<PixelPusher> pushers) {
    GenerateMapping(pushers, false);
  }

  public InetAddress getSacnMulticast(int universe) {
	  byte[] rawAddr = new byte[4];
	  rawAddr[0] = (byte)239;
	  rawAddr[1] = (byte)255;
	  rawAddr[2] = (byte)((universe >> 8) & 0xff);
	  rawAddr[3] = (byte)(universe & 0xff);
	  
	  try {
		return InetAddress.getByAddress(rawAddr);
	  } catch (UnknownHostException e) {
		e.printStackTrace();
		return null;
	}
  }
  
  public void GenerateMapping(List<PixelPusher> pushers, boolean pack) {
    for (PixelPusher pusher : pushers) {
      int startingChannel = pusher.getArtnetChannel();
      int startingUniverse = pusher.getArtnetUniverse();
      System.out.println("Mapping pusher at starting universe "
          + startingUniverse + ", starting channel: " + startingChannel);
      if (startingChannel == 0 && startingUniverse == 0) {
    	  System.out.println("Not mapping.  Set artnet_channel and artnet_universe to something other than 0.");
          continue;
      }
      if (mappedPushers.contains(pusher)) {
    	  System.out.println("Already mapped this pusher.");
      } else {
    	  mappedPushers.add(pusher);
      }
      
      int numberOfStrips = pusher.getNumberOfStrips();
      int pixelsPerStrip = pusher.getPixelsPerStrip();
      int currentUniverse = startingUniverse;
      int currentChannel = startingChannel;
      int currentStrip = 0;
      int currentPixel = 0;
      int totalPixelsLeftToMap = numberOfStrips * pixelsPerStrip;
      InetAddress location;

      while (totalPixelsLeftToMap > 0) {
        // set current pixel's mapping
    	location = getSacnMulticast(currentUniverse);
    	if (pusher.getStrip(currentStrip).getRGBOW()) {
       		System.out.println("ArtNet: RGBOW channels [" + currentUniverse + ", "
    				+ currentChannel + "," + (currentChannel + 1) + ","
    				+ (currentChannel + 2) + ","+ (currentChannel + 3) +","
    				+ (currentChannel + 4) + "] -> PixelPusher: [" + currentStrip
    				+ ", " + currentPixel + "] at multicast "+location);
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel+ArtNetBridge.order.getOffset(ColourOrdering.RED), location),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    				currentPixel, PixelPusherLocation.Channel.RED));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel+ArtNetBridge.order.getOffset(ColourOrdering.GREEN), location),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    				currentPixel, PixelPusherLocation.Channel.GREEN));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel+ArtNetBridge.order.getOffset(ColourOrdering.BLUE), location),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    			    currentPixel, PixelPusherLocation.Channel.BLUE));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 3, location),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    			    currentPixel, PixelPusherLocation.Channel.ORANGE));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 4, location),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    			    currentPixel, PixelPusherLocation.Channel.WHITE));
    		pusher.setLastUniverse(currentUniverse);
    		if (!multicastAddresses.contains(location))
    			multicastAddresses.add(location);
   		
    	} else {
    		System.out.println("ArtNet: RGB channels [" + currentUniverse + ", "
    				+ currentChannel + "," + (currentChannel + 1) + ","
    				+ (currentChannel + 2) + "] -> PixelPusher: [" + currentStrip
    				+ ", " + currentPixel + "] at multicast "+location);
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel+ArtNetBridge.order.getOffset(ColourOrdering.RED), location),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    				currentPixel, PixelPusherLocation.Channel.RED));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel +ArtNetBridge.order.getOffset(ColourOrdering.GREEN), location),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    				currentPixel, PixelPusherLocation.Channel.GREEN));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel +ArtNetBridge.order.getOffset(ColourOrdering.BLUE), location),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    			    currentPixel, PixelPusherLocation.Channel.BLUE));
    		pusher.setLastUniverse(currentUniverse);
    		if (!multicastAddresses.contains(location))
    			multicastAddresses.add(location);
    	}
        // increment pixelpusher pixel index
        currentPixel++;
        totalPixelsLeftToMap--;
        // if pixelpusher pixel == pixels per strip then set pixel to 0 and
        // increment strip index
        // also move on to the next universe
        if (currentPixel == pixelsPerStrip) {
          currentPixel = 0;
          currentStrip++;

          if (currentStrip >= pusher.getNumberOfStrips())
        	  continue;
          
          if (!pack) {
            currentChannel = 1;
            currentUniverse++;
            continue;
          }
        }

        // increment artnet channel by three
        if (pusher.getStrip(currentStrip).getRGBOW()) {
        	currentChannel += 5;
            if (CHANNELS_PER_UNIVERSE - currentChannel < 5) {
                currentChannel = 1;
                currentUniverse++;
              }
        } else {
        	currentChannel += 3;
            if (CHANNELS_PER_UNIVERSE - currentChannel < 3) {
                currentChannel = 1;
                currentUniverse++;
            }
        }
      }
    }
  }
}
