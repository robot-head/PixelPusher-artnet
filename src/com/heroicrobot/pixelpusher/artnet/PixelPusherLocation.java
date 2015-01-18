package com.heroicrobot.pixelpusher.artnet;

import java.util.Map;

import com.heroicrobot.dropbit.devices.pixelpusher.*;
import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class PixelPusherLocation {
  private String macAddr;
  private DeviceRegistry registry; 
  private int strip;
  private int pixel;
  private Channel channel;

  public enum Channel {
    RED, GREEN, BLUE, ORANGE, WHITE
  }

  public PixelPusherLocation(DeviceRegistry registry, String macAddr, int strip, int pixel, Channel channel) {
	this.registry = registry;
	this.macAddr = macAddr;
    this.strip = strip;
    this.pixel = pixel;
    this.channel = channel;
  }

  /**
   * @return the strip
   */
  public Strip getStrip() {
	Map<String, PixelPusher> t = registry.getPusherMap();
	return t.get(macAddr).getStrip(strip);
  }

  /**
   * @param strip
   *          the strip to set
   */
  public void setStrip(Strip strip) {
	this.macAddr = strip.getPusher().getMacAddress();
    this.strip = strip.getStripNumber();
  }

  /**
   * @return the pixel
   */
  public int getPixel() {
    return pixel;
  }

  /**
   * @param pixel
   *          the pixel to set
   */
  public void setPixel(int pixel) {
    this.pixel = pixel;
  }

  /**
   * @return the channel
   */
  public Channel getChannel() {
    return channel;
  }

  /**
   * @param channel
   *          the channel to set
   */
  public void setChannel(Channel channel) {
    this.channel = channel;
  }

}
