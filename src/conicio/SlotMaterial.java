package conicio;

import conicio.util.*;

import java.util.*;

/**
 * 
 **/
public class SlotMaterial extends Material<SlotColor> {
	private final double diffReflectCoeff;
	private final double specReflectCoeff;
	private final double diffTransmitCoeff;
	private final double specTransmitCoeff;
	
	public final double ior;
	
	public final double b1 = 2.0245976;
	public final double b2 = 0.470187196;
	public final double b3 = 2.59970433;
	public final double c1 = 0.0147053225 * 9;
	public final double c2 = 0.0692998276;
	public final double c3 = 161.817601;
	
	/*
	public final double b1 =   1.4313493000;
	public final double b2 =   0.6505471300;
	public final double b3 =   5.3414021000;
	public final double c1 =   0.0052799261;
	public final double c2 =   0.0142382647;
	public final double c3 = 325.0178340000;
	*/

	public SlotMaterial(SlotColor diffReflect, SlotColor specReflect, SlotColor diffTransmit, SlotColor specTransmit, double ior) {
		super(diffReflect, specReflect, diffTransmit, specTransmit);
		this.ior = ior;
		
		SlotColor      reflect = diffReflect.add(specReflect);
		double     probReflect = 0.;
		SlotColor     transmit = diffTransmit.add(specTransmit);
		double    probTransmit = 0.;
		
		/*
		for(int x = 0; x < SlotColor.SAMPLES; x++) { probReflect  = Math.max(probReflect ,  reflect.components.get(x)); }
		for(int x = 0; x < SlotColor.SAMPLES; x++) { probTransmit = Math.max(probTransmit, transmit.components.get(x)); }
		*/
		
		for(int x = 0; x < SlotColor.SAMPLES; x++) { probReflect  +=  reflect.components.get(x) / SlotColor.SAMPLES; }
		for(int x = 0; x < SlotColor.SAMPLES; x++) { probTransmit += transmit.components.get(x) / SlotColor.SAMPLES; }
		
		double diffSumReflect  = 0.;
		double specSumReflect  = 0.;
		double diffSumTransmit = 0.;
		double specSumTransmit = 0.;
		
		for(int x = 0; x < SlotColor.SAMPLES; x++) { diffSumReflect  +=  diffReflect.components.get(x); }
		for(int x = 0; x < SlotColor.SAMPLES; x++) { specSumReflect  +=  specReflect.components.get(x); }
		for(int x = 0; x < SlotColor.SAMPLES; x++) { diffSumTransmit += diffTransmit.components.get(x); }
		for(int x = 0; x < SlotColor.SAMPLES; x++) { specSumTransmit += specTransmit.components.get(x); }
		
		this.diffReflectCoeff  = diffSumReflect  * probReflect  / (diffSumReflect  + specSumReflect );
		this.specReflectCoeff  = specSumReflect  * probReflect  / (diffSumReflect  + specSumReflect );
		this.diffTransmitCoeff = diffSumTransmit * probTransmit / (diffSumTransmit + specSumTransmit);
		this.specTransmitCoeff = specSumTransmit * probTransmit / (diffSumTransmit + specSumTransmit);
	}
	
	public double diffReflectCoeff()  { return diffReflectCoeff;  }
	public double specReflectCoeff()  { return specReflectCoeff;  }
	public double diffTransmitCoeff() { return diffTransmitCoeff; }
	public double specTransmitCoeff() { return specTransmitCoeff; }
	public double ior() { return ior; }
	
	/**
	 *
	 **/
	public Map<Ray, SlotColor> refractTo(Ray ray, Vector3 intersection, Vector3 normal, SlotColor color) {
		Map<Ray, SlotColor> dispersions = new HashMap<Ray, SlotColor>();
		for(int x = 0; x < SlotColor.SAMPLES; x++) {
			if(color.components.get(x) > 0.) {
				double lambda = SlotColor.wavelengths.get(x) / 1000;
				double ior2 = Math.sqrt(
					1 + (b1 * lambda * lambda) / (lambda * lambda - c1)
					  + (b2 * lambda * lambda) / (lambda * lambda - c2)
					  + (b3 * lambda * lambda) / (lambda * lambda - c3)
				);
				//System.out.println((ior2 - 1.73) * 20 + 1.73);
				
				Vector3 inner = ray.normal.refract(normal, 1.0, ior2);
				//Vector3 inner = ray.normal.refract(normal, 1.0, (ior2 - 1.73) * 10 + 1.20);
				if(inner != null) {
					Double[] amplitudes = new Double[SlotColor.SAMPLES];
					for(int y = 0; y < SlotColor.SAMPLES; y++) { amplitudes[y] = 0.; }
					amplitudes[x] = color.components.get(x);
				
					dispersions.put(new Ray(intersection, inner), new SlotColor(amplitudes));
				}
			}
		}
		
		return dispersions;
	}
	
	/**
	 *
	 **/
	public Map<Ray, SlotColor> refractFrom(Ray ray, Vector3 intersection, Vector3 normal, SlotColor color) {
		Map<Ray, SlotColor> dispersions = new HashMap<Ray, SlotColor>();
		for(int x = 0; x < SlotColor.SAMPLES; x++) {
			if(color.components.get(x) > 0.) {
				double lambda = SlotColor.wavelengths.get(x) / 1000;
				double ior2 = Math.sqrt(
					1 + (b1 * lambda * lambda) / (lambda * lambda - c1)
					  + (b2 * lambda * lambda) / (lambda * lambda - c2)
					  + (b3 * lambda * lambda) / (lambda * lambda - c3)
				);
				
				Vector3 outer = ray.normal.refract(normal.neg(), ior2, 1.0);
				//Vector3 outer = ray.normal.refract(normal.neg(), (ior2 - 1.73) * 10 + 1.20, 1.0);
				if(outer != null) {
					Double[] amplitudes = new Double[SlotColor.SAMPLES];
					for(int y = 0; y < SlotColor.SAMPLES; y++) { amplitudes[y] = 0.; }
					amplitudes[x] = color.components.get(x);
				
					dispersions.put(new Ray(intersection, outer), new SlotColor(amplitudes));
				}
			}
		}
		
		return dispersions;
	}
	
	public static final SlotMaterial GLASS = new SlotMaterial(
		SlotColor.BLACK,
		//new SlotColor(new Range(380, 780, 0.6 / SlotColor.SAMPLES)),
		SlotColor.BLACK,
		SlotColor.BLACK,
		//new SlotColor(new Range(380, 780, 2.4 / SlotColor.SAMPLES)),
		//new SlotColor(new Range(380, 780, 3.0 / SlotColor.SAMPLES)),
		new SlotColor(new Range(380, 780, 1.0)),
		1.30
	);
	public static final SlotMaterial CHROME = new SlotMaterial(
		new SlotColor(new Range(380, 780, 2.1 / SlotColor.SAMPLES)),
		new SlotColor(new Range(380, 780, 0.3 / SlotColor.SAMPLES)),
		SlotColor.BLACK,
		SlotColor.BLACK,
		0.0
	);
}
