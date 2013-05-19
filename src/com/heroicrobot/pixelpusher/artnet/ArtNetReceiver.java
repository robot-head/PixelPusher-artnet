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

  public ArtNetReceiver() {
    buf = new byte[576];
  }

  private void update_channel(int universe, int channel, int value) {

  }

  private void parseArtnetPacket(DatagramPacket packet) {
    buf = packet.getData();
    for (int i=0; header[i]>0; i++) {
      if (header[i] != buf[i])
        return;
    }
    // If we get here, it looks like there's a packet to handle.
    if (buf[8] == 0x50 && buf[9] == 0x00) {
      // Opcode 0x5000 is DMX data
      int universe = buf[14] & (buf[15]<<8);

         for(int i=0; i< 512; i++) {
           // the channel data is in buf[i+17];
           update_channel(universe, i, buf[i+17]);
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
    while (true) {
      try {
        socket.receive(packet);
        parseArtnetPacket(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
