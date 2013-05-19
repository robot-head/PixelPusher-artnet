package com.heroicrobot.pixelpusher.artnet;

public class ArtNetLocation {

  private int universe;
  private int channel;

  /**
   * @return the universe
   */
  public int getUniverse() {
    return universe;
  }
  /**
   * @param universe the universe to set
   */
  public void setUniverse(int universe) {
    this.universe = universe;
  }
  /**
   * @return the channel
   */
  public int getChannel() {
    return channel;
  }
  /**
   * @param channel the channel to set
   */
  public void setChannel(int channel) {
    this.channel = channel;
  }

  public ArtNetLocation(int universe, int channel) {
    this.universe = universe;
    this.channel = channel;
  }


}
