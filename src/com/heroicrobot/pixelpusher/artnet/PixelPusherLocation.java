package com.heroicrobot.pixelpusher.artnet;

public class PixelPusherLocation {
  private int strip;
  private int pixel;
  private Channel channel;

  public enum Channel{
    RED, GREEN, BLUE, ORANGE, WHITE
  }

  public PixelPusherLocation(int strip, int pixel, Channel channel) {
    this.strip = strip;
    this.pixel = pixel;
    this.channel = channel;
  }

  /**
   * @return the strip
   */
  public int getStrip() {
    return strip;
  }

  /**
   * @param strip the strip to set
   */
  public void setStrip(int strip) {
    this.strip = strip;
  }

  /**
   * @return the pixel
   */
  public int getPixel() {
    return pixel;
  }

  /**
   * @param pixel the pixel to set
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
   * @param channel the channel to set
   */
  public void setChannel(Channel channel) {
    this.channel = channel;
  }

}
