package com.heroicrobot.pixelpusher.artnet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;

public class ArtNetMapping {

  public static final int CHANNELS_PER_UNIVERSE = 512;

  Map<ArtNetLocation, PixelPusherLocation> mapping;

  ArtNetMapping() {
    mapping = new HashMap<ArtNetLocation, PixelPusherLocation>();
  }

  public PixelPusherLocation getPixelPusherLocation(int universe, int channel) {
    ArtNetLocation loc = new ArtNetLocation(universe, channel);
    return this.mapping.get(loc);
  }

  public void GenerateMapping(List<PixelPusher> pushers) {
    GenerateMapping(pushers, false);
  }

  public void GenerateMapping(List<PixelPusher> pushers, boolean pack) {
    for (PixelPusher pusher : pushers) {
      int startingChannel = pusher.getArtnetChannel();
      int startingUniverse = pusher.getArtnetUniverse();
      System.out.println("Mapping pusher at starting universe "
          + startingUniverse + ", starting channel: " + startingChannel);
      if (startingChannel == 0 && startingUniverse == 0)
        continue;
      int numberOfStrips = pusher.getNumberOfStrips();
      int pixelsPerStrip = pusher.getPixelsPerStrip();
      int currentUniverse = startingUniverse;
      int currentChannel = startingChannel;
      int currentStrip = 0;
      int currentPixel = 0;
      int totalPixelsLeftToMap = numberOfStrips * pixelsPerStrip;

      while (totalPixelsLeftToMap > 0) {
        // set current pixel's mapping
    	if (pusher.getStrip(currentStrip).getRGBOW()) {
       		System.out.println("ArtNet: RGBOW channels [" + currentUniverse + ", "
    				+ currentChannel + "," + (currentChannel + 1) + ","
    				+ (currentChannel + 2) + ","+ (currentChannel + 3) +","
    				+ (currentChannel + 4) + "] -> PixelPusher: [" + currentStrip
    				+ ", " + currentPixel + "]");
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    				currentPixel, PixelPusherLocation.Channel.RED));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 1),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    				currentPixel, PixelPusherLocation.Channel.GREEN));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 2),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    			    currentPixel, PixelPusherLocation.Channel.BLUE));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 3),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    			    currentPixel, PixelPusherLocation.Channel.ORANGE));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 4),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    			    currentPixel, PixelPusherLocation.Channel.WHITE));
   		
    	} else {
    		System.out.println("ArtNet: RGB channels [" + currentUniverse + ", "
    				+ currentChannel + "," + (currentChannel + 1) + ","
    				+ (currentChannel + 2) + "] -> PixelPusher: [" + currentStrip
    				+ ", " + currentPixel + "]");
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    				currentPixel, PixelPusherLocation.Channel.RED));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 1),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    				currentPixel, PixelPusherLocation.Channel.GREEN));
    		mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 2),
    				new PixelPusherLocation(pusher.getStrip(currentStrip),
    			    currentPixel, PixelPusherLocation.Channel.BLUE));
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
