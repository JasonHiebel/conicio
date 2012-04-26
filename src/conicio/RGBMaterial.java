package conicio;

import conicio.util.*;

import java.util.*;

/**
 * 
 **/
public class RGBMaterial extends Material<RGBColor> {
	private final double diffReflectCoeff;
	private final double specReflectCoeff;
	private final double diffTransmitCoeff;
	private final double specTransmitCoeff;
	
	public final double ior;

	public RGBMaterial(RGBColor diffReflect, RGBColor specReflect, RGBColor diffTransmit, RGBColor specTransmit, double ior) {
		super(diffReflect, specReflect, diffTransmit, specTransmit);
		this.ior = ior;
		
		RGBColor      reflect = diffReflect.add(specReflect);
		//double    probReflect = Math.max(Math.max(reflect.r, reflect.g), reflect.b);
		double    probReflect = (reflect.r + reflect.g + reflect.b) / 3;
		RGBColor     transmit = diffTransmit.add(specTransmit);
		//double   probTransmit = Math.max(Math.max(transmit.r, transmit.g), transmit.b);
		double   probTransmit = (transmit.r + transmit.g + transmit.b) / 3;
		
		double diffSumReflect  =  diffReflect.r +  diffReflect.g +  diffReflect.b;
		double specSumReflect  =  specReflect.r +  specReflect.g +  specReflect.b;
		double diffSumTransmit = diffTransmit.r + diffTransmit.g + diffTransmit.b;
		double specSumTransmit = specTransmit.r + specTransmit.g + specTransmit.b;
		
		this.diffReflectCoeff  = diffSumReflect * probReflect  / (diffSumReflect  + specSumReflect );
		this.specReflectCoeff  = specSumReflect * probReflect  / (diffSumReflect  + specSumReflect );
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
	public Map<Ray, RGBColor> refractTo(Ray ray, Vector3 intersection, Vector3 normal, RGBColor color) {
		Vector3 inner = ray.normal.refract(normal, 1.0, ior);
		if(inner == null) { return new HashMap<Ray, RGBColor>(); }
		else { 
			return Collections.singletonMap(new Ray(intersection, inner), color);
		}
	}
	
	/**
	 *
	 **/
	public Map<Ray, RGBColor> refractFrom(Ray ray, Vector3 intersection, Vector3 normal, RGBColor color) {
		Vector3 outer = ray.normal.refract(normal.neg(), ior, 1.0);
		if(outer == null) { return new HashMap<Ray, RGBColor>(); }
		else { 
			return Collections.singletonMap(new Ray(intersection, outer), color);
		}
	}
	
	public static final RGBMaterial GLASS = new RGBMaterial(
		new RGBColor(0.000000, 0.000000, 0.000000),
		new RGBColor(0.000000, 0.000000, 0.000000),
		new RGBColor(0.000000, 0.000000, 0.000000), 
		new RGBColor(1.000000, 1.000000, 1.000000), 1.70);
	public static final RGBMaterial CHROME = new RGBMaterial(
		new RGBColor(0.700000, 0.700000, 0.700000),
		new RGBColor(0.100000, 0.100000, 0.100000),
		new RGBColor(0.000000, 0.000000, 0.000000),
		new RGBColor(0.000000, 0.000000, 0.000000), 0.00);
}
