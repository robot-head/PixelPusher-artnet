package com.heroicrobot.pixelpusher.artnet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ArtNetReceiver extends Thread {

  public static final int ARTNET_PORT = 6454;
  public static final byte[] header = { 0x41, 0x72, 0x74, 0x2d, 0x4e, 0x65,
      0x74, 0x0 };

  byte[] buf;
  PixelPusherObserver observer;

  public ArtNetReceiver(PixelPusherObserver observer) {
    this.observer = observer;
    buf = new byte[576];
  }

  private void update_channel(int universe, int channel, int value) {
    // System.out.println("Universe " + universe + " channel " + channel
    // + " value " + value);
    try {
      PixelPusherLocation loc = observer.mapping.getPixelPusherLocation(
          universe, channel);
      // TODO: Extract color component from value
      switch (loc.getChannel()) {
        case RED:
          loc.getStrip().setPixelRed((byte) value, loc.getPixel());
          break;
        case GREEN:
          loc.getStrip().setPixelGreen((byte) value, loc.getPixel());
          break;
        case BLUE:
          loc.getStrip().setPixelBlue((byte) value, loc.getPixel());
          break;
        default:
          break;
      }

    } catch (NullPointerException e) {
  //      System.out.println("No pixel at universe " + universe + " channel "
  //          + channel);
    }
  }

  private void parseArtnetPacket(DatagramPacket packet) {
    buf = packet.getData();
    for (int i = 0; header[i] > 0; i++) {
      if (header[i] != buf[i])
        return;
    }
    // If we get here, it looks like there's a packet to handle.
    if (buf[8] == 0x00 && buf[9] == 0x50) {
      // Opcode 0x5000 is DMX data
      int universe = (buf[14] | (buf[15] << 8)) + 1;
      for (int i = 0; i < 512; i++) {
        // the channel data is in buf[i+17];
        update_channel(universe, i + 1, buf[i + 18]);
      }

    } else {
      return;
    }
  }

  @Override
  public void run() {
    DatagramSocket socket = null;
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    try {
      socket = new DatagramSocket(ARTNET_PORT, InetAddress.getByName("0.0.0.0"));
      socket.setBroadcast(true);
      System.out.println("Listen on " + socket.getLocalAddress() + " from "
          + socket.getInetAddress() + " port " + socket.getBroadcast());
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    int packetno = 0;
    while (true) {
      try {
        socket.receive(packet);
        parseArtnetPacket(packet);
        if (packetno % 100 == 0)
//          System.out.println("Got a packet");
        // System.out.println(Arrays.toString(packet.getData()));
        packetno++;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
