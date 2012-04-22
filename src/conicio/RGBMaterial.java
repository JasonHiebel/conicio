package conicio;

import conicio.util.*;

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
		double    probReflect = Math.max(Math.max(reflect.r, reflect.g), reflect.b);
		RGBColor     transmit = diffTransmit.add(specTransmit);
		double   probTransmit = Math.max(Math.max(transmit.r, transmit.g), transmit.b);
		
		double diffSumReflect  =  diffReflect.r +  diffReflect.g +  diffReflect.b;
		double specSumReflect  =  specReflect.r +  specReflect.g +  specReflect.b;
		double diffSumTransmit = diffTransmit.r + diffTransmit.g + diffTransmit.b;
		double specSumTransmit = specTransmit.r + specTransmit.g + specTransmit.b;
		
		this.diffReflectCoeff  = diffSumReflect * probReflect  / (diffSumReflect  + specSumReflect );
		this.specReflectCoeff  = specSumReflect * probReflect  / (diffSumReflect  + specSumReflect );
		this.diffTransmitCoeff = diffSumReflect * probTransmit / (diffSumTransmit + specSumTransmit);
		this.specTransmitCoeff = specSumReflect * probTransmit / (diffSumTransmit + specSumTransmit);
	}
	
	public double diffReflectCoeff()  { return diffReflectCoeff;  }
	public double specReflectCoeff()  { return specReflectCoeff;  }
	public double diffTransmitCoeff() { return diffTransmitCoeff; }
	public double specTransmitCoeff() { return specTransmitCoeff; }
	public double ior() { return ior; }
	
	public static final RGBMaterial GLASS = new RGBMaterial(
		new RGBColor(0.000000, 0.000000, 0.000000),
		new RGBColor(0.200000, 0.200000, 0.200000),
		new RGBColor(0.000000, 0.000000, 0.000000), 
		new RGBColor(0.800000, 0.800000, 0.800000), 1.30);
	public static final RGBMaterial CHROME = new RGBMaterial(
		new RGBColor(0.700000, 0.700000, 0.700000),
		new RGBColor(0.100000, 0.100000, 0.100000),
		new RGBColor(0.000000, 0.000000, 0.000000),
		new RGBColor(0.000000, 0.000000, 0.000000), 0.00);
}
