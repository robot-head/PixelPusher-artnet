package com.heroicrobot.pixelpusher.artnet;

// 239.255.0.1

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class SacnReceiver extends Thread {


	public static final int SOURCE_NAME_ADDR  = 44;

	//The ACN identifier string is defined to be "ASC-E1.17\0\0\0";
	public static final int ACN_IDENTIFIER_SIZE = 12;
	public static final byte[] ACN_IDENTIFIER =  {0x41, 0x53, 0x43, 0x2d, 0x45, 0x31, 0x2e, 0x31, 0x37, 0x00, 0x00, 0x00 };
	//The well-known streaming ACN port (currently the ACN port)
	public static final int SACN_PORT = 5568;
	public MulticastSocket mcSocket;
	boolean seenPacket = false;
	private byte[] buf;

	private PixelPusherObserver observer;
	
	/*
	 * 
		struct sacn_node_s {
		  
		  //Root Layer						// Field offsets:
		  uint16_t preamble;				// 0
		  uint16_t postamble;				// 2
		  uint8_t  packetId		[12];		// 4
		  uint16_t flagsLength;				// 14
		  uint8_t  vector		[4];		// 16
		  uint8_t  CID			[16];		// 20
		  //Framing Layer
		  uint16_t frmFlagsLength;			// 36
		  uint8_t  frmVector	[4];		// 38
		  uint8_t  sourceName	[64];		// 42
		  uint8_t  priority;				// 106
		  uint8_t  reservedIgnore	[2];	// 107
		  uint8_t  seqNo;					// 109
		  uint8_t  options;					// 110
		  uint8_t  universeNo      [2];		// 111
		  //DMP Layer
		  uint16_t dmpFlagsLength;			// 113
		  uint8_t  dmpVector;				// 115
		  uint8_t  addrType;				// 116
		  uint16_t firstPropertyAddr;		// 118
		  uint16_t addrIncrement;			// 120
		  uint16_t propValCount;			// 122
		  uint8_t  dataValues	[513];		// 124  NOTE:  dmx data starts at dataValues + 1
		  
		} __attribute__((packed));
	 */
	
	 public SacnReceiver(PixelPusherObserver observer) {
		    this.observer = observer;
		    buf = new byte[680];
		    this.seenPacket = false;
		    try {
				mcSocket = new MulticastSocket(SACN_PORT);
			//	mcSocket.setReuseAddress(true);
				//mcSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), SACN_PORT));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    
	 }
	
	 public void addGroup(InetAddress group) {
		 try {
			System.out.println("Joining multicast group "+group);
			mcSocket.joinGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
	 private void update_channel(int universe, int channel, int value) {
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
		      case ORANGE:
		        loc.getStrip().setPixelOrange((byte) value, loc.getPixel());
		        break;
		      case WHITE:
		        loc.getStrip().setPixelWhite((byte) value, loc.getPixel());
		        break;
		      default:
		        break;
		      }

		    } catch (NullPointerException e) {
		      // System.out.println("No pixel at universe " + universe +
		      // " channel "
		      // + channel);
		    }
		  }
	
	  private void parseSacnPacket(DatagramPacket packet) {
		    buf = packet.getData();
		    for (int i = 0; i < ACN_IDENTIFIER_SIZE; i++) {
		      if (ACN_IDENTIFIER[i] != buf[i + 4]) { // packetId
		    	  System.out.println("Got a packet on the sACN port, but ID was wrong.");
		    	  return;
		      }
		    }
		    // If we get here, it looks like there's a packet to handle.
		    if (buf[8] == 0x00 && buf[9] == 0x50) {
		      // Opcode 0x5000 is DMX data
		      if (!this.seenPacket) {
		        System.out.println("Got an sACN packet!");
		        this.seenPacket = true;
		      }
		      int universe = (buf[111] | (buf[112] << 8)) + 1;
		      for (int i = 0; i < 512; i++) {
		        // the channel data is in buf[i+124];
		        update_channel(universe, i + 1, buf[i + 125]);
		      }
		    } 
		  }
	
	  @Override
	  public void run() {
	  //  DatagramSocket socket = null;
	    DatagramPacket packet = new DatagramPacket(buf, buf.length);

	  //  try {
	      // socket = new DatagramSocket(null);
	      
	     // socket.setReuseAddress(true);
	     // socket.setBroadcast(true);
	      
	     // socket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), SACN_PORT));
	     // System.out.println("Listening for streaming ACN messages on " + socket.getLocalAddress() + " port "
	     //     + socket.getLocalPort() + ", broadcast=" + socket.getBroadcast());
	  //  } catch (IOException e) {
	   //   e.printStackTrace();
	   //   return;
	   // }
	    int packetno = 0;
	    while (true) {
	      try {
	    	packet.setLength(buf.length);
	        mcSocket.receive(packet);
	        parseSacnPacket(packet);
	        if (packetno % 100 == 0)
	          packetno++;
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	      
	    }
	  }
}
