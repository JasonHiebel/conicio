package conicio.util;

import conicio.*;
import java.util.*;

/**
 * A three component, immutable RGB color representation.
 *
 * @author Jason Hiebel
 **/
public class SlotColor implements Color<SlotColor> {
	/** **/
	public static final int SAMPLES = 16;

	/** **/
	public static final List<Double> wavelengths;
	static {
		double lRange = 380.;
		double rRange = 780.;
		double delta  = (rRange - lRange) / SAMPLES;
	
		List<Double> backing = new ArrayList<Double>();
		for(int x = 0; x < SAMPLES; x++) { backing.add(lRange + delta * (x + .5)); }
		wavelengths = Collections.unmodifiableList(backing);
		System.out.println(wavelengths);
	}
	
	/** The color black **/
	public static final SlotColor BLACK = new SlotColor();
	
	/** The color white **/
	public static final SlotColor WHITE = new SlotColor(new Range(380, 780, 1));
	
	/** The color factory, resposible for default colors **/
	public static final Factory FACTORY = new Factory();


	/** **/
	public final List<Double> components;

	/** The packed integer representation. **/
	public final int rgb;

	/**
	 *
	 **/
	public SlotColor(Range... ranges) {
		List<Double> backing = new ArrayList<Double>();
		for(int x = 0; x < SAMPLES; x++) {
			double delta  = (780. - 380.) / SAMPLES;
			double lRange = wavelengths.get(x) - delta / 2;
			double rRange = wavelengths.get(x) + delta / 2;
			
			double amplitude = 0.;
			for(Range range : ranges) {
				if     (lRange <= range.low  && range.low  <= rRange) { amplitude += range.amplitude * (rRange     - range.low) / delta; }
				else if(lRange <= range.high && range.high <= rRange) { amplitude += range.amplitude * (range.high - lRange   ) / delta; }
				else if(range.low <= lRange  && rRange <= range.high) { amplitude += range.amplitude;                                    }
			}
			
			backing.add(amplitude);
		}
		this.components = Collections.unmodifiableList(backing);
		
		RGBColor color = RGBColor.BLACK;
		for(int x = 0; x < SAMPLES; x++) { color = color.add(burtons(wavelengths.get(x)).scale(components.get(x))); }
		this.rgb = color.asRGB();
	}
	
	/** **/
	public SlotColor(Double[] amplitudes) {
		if(amplitudes.length != SAMPLES) {}
		
		for(int x = 0; x < SAMPLES; x++) { amplitudes[x] = clamp(amplitudes[x], 0, Double.POSITIVE_INFINITY); }
		this.components = Collections.unmodifiableList(Arrays.asList(amplitudes));
		
		RGBColor color = RGBColor.BLACK;
		for(int x = 0; x < SAMPLES; x++) { color = color.add(burtons(wavelengths.get(x)).scale(components.get(x))); }
		this.rgb = color.asRGB();
	}

	@Override
	public SlotColor add(SlotColor c) {
		Double[] amplitudes = new Double[SAMPLES];
		for(int x = 0; x < SAMPLES; x++) { amplitudes[x] = components.get(x) + c.components.get(x); }
		return new SlotColor(amplitudes);
	}

	@Override
	public SlotColor mask(SlotColor c) {
		Double[] amplitudes = new Double[SAMPLES];
		for(int x = 0; x < SAMPLES; x++) { amplitudes[x] = components.get(x) * c.components.get(x); }
		return new SlotColor(amplitudes);
	}

	@Override
	public SlotColor scale(double s) {
		Double[] amplitudes = new Double[SAMPLES];
		for(int x = 0; x < SAMPLES; x++) { amplitudes[x] = components.get(x) * s; }
		return new SlotColor(amplitudes);
	}
	
	@Override
	public double power() {
		double power = 0;
		for(double component : components) { power += component; }
		return power;
	}
	
	@Override
	public int asRGB() {
		return rgb;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof SlotColor)) { return false; }
		SlotColor c = (SlotColor)o;
		for(int x = 0; x < SAMPLES; x++) { if(this.components.get(x) != c.components.get(x)) { return false; } }
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[ SlotColor : ");
		for(int x = 0; x < SAMPLES; x++) { builder.append(wavelengths.get(x) + ";" + components.get(x) + "  "); }
		return builder.toString();
	}
	
	//
	private static double clamp(double val, double min, double max) {
		if(Double.isNaN(val)) { return 0; }
		return Math.max(Math.min(val, max), min);
	}
	
	//
	private static double interpolate(double val, double lRange, double rRange, double min, double max) {
		return (val - lRange) * (max - min) / (rRange - lRange) + min;
	}

	//
	private static RGBColor burtons(double val) {
		double r = 0.;
		if     (380 < val && val < 440) { r = interpolate(val, 380, 440, 0.4, 0.0); }
		else if(510 < val && val < 580) { r = interpolate(val, 510, 580, 0.0, 1.0); }
		else if(580 < val && val < 710) { r = 1.0;                                  }
		else if(710 < val && val < 780) { r = interpolate(val, 710, 780, 1.0, 0.4); }
		
		double g = 0.;
		if     (440 < val && val < 490) { g = interpolate(val, 440, 490, 0.0, 1.0); }
		else if(500 < val && val < 580) { g = 1.0;                                  }
		else if(580 < val && val < 650) { g = interpolate(val, 580, 650, 1.0, 0.0); }
		
		double b = 0.;
		if     (380 < val && val < 425) { b = interpolate(val, 380, 425, 0.4, 1.0); }
		else if(425 < val && val < 500) { b = 1.0;                                  }
		else if(500 < val && val < 510) { b = interpolate(val, 500, 510, 1.0, 0.0); }
		
		return new RGBColor(r, g, b);
	}

	/**
	 *
	 **/
	public static class Factory implements ColorFactory<SlotColor> {
		//
		private Factory() { }
		
		@Override
		public SlotColor black() { return BLACK; }
		
		@Override
		public SlotColor white() { return WHITE; }
	}
}