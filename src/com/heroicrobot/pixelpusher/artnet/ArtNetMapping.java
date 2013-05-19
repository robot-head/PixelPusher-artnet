package com.heroicrobot.pixelpusher.artnet;

import java.util.HashMap;
import java.util.Map;

import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;

public class ArtNetMapping {

  public static final int CHANNELS_PER_UNIVERSE = 512;

  Map<ArtNetLocation, PixelPusherLocation> mapping;

  ArtNetMapping() {
    mapping = new HashMap<ArtNetLocation, PixelPusherLocation>();
  }

  public void GenerateMapping(PixelPusher pusher) {
    int startingChannel = pusher.getArtnetChannel();
    int startingUniverse = pusher.getArtnetUniverse();
    int numberOfStrips = pusher.getNumberOfStrips();
    int pixelsPerStrip = pusher.getPixelsPerStrip();
    int currentUniverse = startingUniverse;
    int currentChannel = startingChannel;
    int currentStrip = 0;
    int currentPixel = 0;
    int totalPixelsLeftToMap = numberOfStrips * pixelsPerStrip;

    while (totalPixelsLeftToMap > 0) {
      // set current pixel's mapping
      System.out.println("ArtNet: [" + currentUniverse + ", " + currentChannel
          +"] -> PixelPusher: ["+currentStrip+", "+currentPixel+"]");
      mapping.put(new ArtNetLocation(currentUniverse, currentChannel),
          new PixelPusherLocation(currentStrip, currentPixel,
              PixelPusherLocation.Channel.RED));
      mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 1),
          new PixelPusherLocation(currentStrip, currentPixel,
              PixelPusherLocation.Channel.GREEN));
      mapping.put(new ArtNetLocation(currentUniverse, currentChannel + 2),
          new PixelPusherLocation(currentStrip, currentPixel,
              PixelPusherLocation.Channel.BLUE));

      // increment pixelpusher pixel index
      currentPixel++;
      totalPixelsLeftToMap--;
      // if pixelpusher pixel == pixels per strip then set pixel to 0 and
      // increment strip index
      // also move on to the next universe
      if (currentPixel == pixelsPerStrip) {
        currentPixel = 0;
        currentStrip++;
        currentChannel = 1;
        currentUniverse++;
        continue;
      }

      // increment artnet channel by three
      currentChannel += 3;
      if (currentChannel > CHANNELS_PER_UNIVERSE) {
        currentChannel = 1;
        currentUniverse++;
      }
    }

  }
}
