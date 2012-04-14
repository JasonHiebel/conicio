package conicio.util;

import conicio.*;

/**
 * A three component, immutable RGB color representation.
 *
 * @author Jason Hiebel
 **/
public class RGBColor implements Color<RGBColor> {
	/** The color black **/
	public static final RGBColor BLACK = new RGBColor(0, 0, 0);
	
	/** The color white **/
	public static final RGBColor WHITE = new RGBColor(1, 1, 1);
	
	/** The color factory, resposible for default colors **/
	public static final Factory FACTORY = new Factory();

	/** The red component. **/
	public final double r;

	/** The green component. **/
	public final double g;

	/** The blue component. **/
	public final double b;
	
	/** The packed integer representation. **/
	public final int rgb;

	/**
	 *
	 *
	 * @param r
	 * @param g
	 * @param b
	 **/
	public RGBColor(double r, double g, double b) {
		this.r = clamp(r, 0, 1);
		this.g = clamp(g, 0, 1);
		this.b = clamp(b, 0, 1);
		
		this.rgb = 0xFF000000         |
			(int)(this.r * 255) << 16 |
			(int)(this.g * 255) <<  8 |
			(int)(this.b * 255) <<  0;	
	}

	@Override
	public RGBColor add(RGBColor c) {
		return new RGBColor(r + c.r, g + c.g, b + c.b);
	}

	@Override
	public RGBColor mask(RGBColor c) {
		return new RGBColor(r * c.r, g * c.g, b * c.b);
	}

	@Override
	public RGBColor scale(double s) {
		return new RGBColor(r * s, g * s, b * s);
	}
	
	@Override
	public int asRGB() {
		return rgb;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof RGBColor)) { return false; }
		RGBColor c = (RGBColor)o;
		return this.r == c.r && this.g == c.g && this.b == c.b;
	}

	@Override
	public String toString() {
		return String.format("[ RGBColor : %f, %f, %f ]", r, g, b);
	}
	
	//
	private static double clamp(double val, double min, double max) {
		if(Double.isNaN(val)) { return 0; }
		return Math.max(Math.min(val, max), min);
	}

	/**
	 *
	 **/
	public static class Factory implements ColorFactory<RGBColor> {
		//
		private Factory() { }
		
		@Override
		public RGBColor black() { return BLACK; }
		
		@Override
		public RGBColor white() { return WHITE; }
	}
	
	public static final Material<RGBColor> CHROME = new Material<RGBColor>(
		new RGBColor(0.250000, 0.250000, 0.250000),
		new RGBColor(0.400000, 0.400000, 0.400000),
		new RGBColor(0.774597, 0.774597, 0.774597), 76.8, 1.00, 0.00, 0.20);
	public static final Material<RGBColor> EMERALD = new Material<RGBColor>(
		new RGBColor(0.021500, 0.174500, 0.021500),
		new RGBColor(0.075680, 0.614240, 0.075680),
		new RGBColor(0.633000, 0.727811, 0.633000), 76.8, 0.55, 1.56, 0.30); 
	public static final Material<RGBColor> TURQUOISE = new Material<RGBColor>(
		new RGBColor(0.100000, 0.187250, 0.174500),
		new RGBColor(0.396000, 0.741510, 0.691020),
		new RGBColor(0.297254, 0.308290, 0.306678), 12.8, 0.80, 1.61, 0.10);
	public static final Material<RGBColor> GLASS = new Material<RGBColor>(
		new RGBColor(1.000000, 1.000000, 1.000000),
		new RGBColor(1.000000, 1.000000, 1.000000),
		new RGBColor(1.000000, 1.000000, 1.000000), 90.0, 0.00, 1.40, 0.30);
}