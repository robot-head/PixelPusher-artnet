package com.heroicrobot.pixelpusher.artnet;

public class ColourOrdering {
	public static final int UNKNOWN = 0;
	public static final int RGB = 1;	
	public static final int RBG = 2;
	public static final int BGR = 3;
	public static final int BRG = 4;
	public static final int GRB = 5;
	public static final int GBR = 6;
	
	public static final int RED = 1;
	public static final int BLUE = 2;
	public static final int GREEN = 3;

	private int order = UNKNOWN;

	ColourOrdering(int order) {
		this.order = order;
	}
	
	ColourOrdering(String orderString) {
		if (orderString.equalsIgnoreCase("RGB"))
			this.order = RGB;
		if (orderString.equalsIgnoreCase("RBG"))
			this.order = RBG;
		if (orderString.equalsIgnoreCase("GRB"))
			this.order = GRB;
		if (orderString.equalsIgnoreCase("GBR"))
			this.order = GRB;
		if (orderString.equalsIgnoreCase("BRG"))
			this.order = BRG;
		if (orderString.equalsIgnoreCase("BGR"))
			this.order = BGR;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public int getOffset(int channel) {
		if (channel == RED) {
			switch (order) {
				case RGB: return 0;
				case RBG: return 0;
				case BGR: return 2;
				case BRG: return 1;
				case GRB: return 1;
				case GBR: return 2;
			}
			return -1;
		}
		if (channel == BLUE) {
			switch (order) {
				case RGB: return 2;
				case RBG: return 1;
				case BGR: return 0;
				case BRG: return 0;
				case GRB: return 2;
				case GBR: return 1;
			}
			return -1;
		}
		if (channel == GREEN) {
			switch (order) {
				case RGB: return 1;
				case RBG: return 2;
				case BGR: return 1;
				case BRG: return 2;
				case GRB: return 0;
				case GBR: return 0;
			}
			return -1;
		}
		// still here?  then bail.
		return -1;
	}
	
}
