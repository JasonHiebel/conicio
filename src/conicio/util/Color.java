package conicio.util;

/**
 * A three component, immutable RGB color representation.
 *
 * @author Jason Hiebel
 **/
public class Color {
	/** The color black **/
	public static final Color BLACK = new Color(0, 0, 0);

	/** The red component.   **/
	public final double r;

	/** The green component. **/
	public final double g;

	/** The blue component.  **/
	public final double b;
	
	// the packed integer representation
	public final int rgb;

	/**
	 *
	 *
	 * @param r
	 * @param g
	 * @param b
	 **/
	public Color(double r, double g, double b) {
		this.r = clamp(r, 0, 1);
		this.g = clamp(g, 0, 1);
		this.b = clamp(b, 0, 1);
		
		this.rgb = 0xFF000000         |
			(int)(this.r * 255) << 16 |
			(int)(this.g * 255) <<  8 |
			(int)(this.b * 255) <<  0;	
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 **/
	public Color add(Color c) {
		return new Color(r + c.r, g + c.g, b + c.b);
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 **/
	public Color mask(Color c) {
		return new Color(r * c.r, g * c.g, b * c.b);
	}

	/**
	 *
	 *
	 * @param s
	 * @return
	 **/
	public Color scale(double s) {
		return new Color(r * s, g * s, b * s);
	}
	
	/**
	 *
	 **/
	public int asRGB() {
		return rgb;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 **/
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Color)) { return false; }
		Color c = (Color)o;
		return this.r == c.r && this.g == c.g && this.b == c.b;
	}

	/**
	 *
	 *
	 * @return
	 **/
	@Override
	public String toString() {
		return String.format("[ Color : %f, %f, %f ]", r, g, b);
	}
	
	//
	private static double clamp(double val, double min, double max) {
		if(Double.isNaN(val)) { return 0; }
		return Math.max(Math.min(val, max), min);
	}

}