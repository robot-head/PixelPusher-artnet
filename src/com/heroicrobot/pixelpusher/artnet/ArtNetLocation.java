package com.heroicrobot.pixelpusher.artnet;

public class ArtNetLocation {

  private int universe;
  private int channel;

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + channel;
    result = prime * result + universe;
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ArtNetLocation other = (ArtNetLocation) obj;
    if (channel != other.channel)
      return false;
    if (universe != other.universe)
      return false;
    return true;
  }

  /**
   * @return the universe
   */
  public int getUniverse() {
    return universe;
  }

  /**
   * @param universe
   *          the universe to set
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
   * @param channel
   *          the channel to set
   */
  public void setChannel(int channel) {
    this.channel = channel;
  }

  public ArtNetLocation(int universe, int channel) {
    this.universe = universe;
    this.channel = channel;
  }

}
