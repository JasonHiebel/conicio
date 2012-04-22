package conicio;

import conicio.util.*;

/**
 * The ways light can interact with a material can be described based on two
 * sets; the first includes the properties diffuse, specular, and glossy and
 * the second includes the interactions transmission and reflection. The
 * materials provided for use in this ray tracer are those of diffuse and
 * specular reflection and specular transmission. Diffuse transmission may be
 * added later to support "dense" pockets which absorb energy locally. Glossy
 * representations will most likely never be implemented directly as the other
 * forms have been, and instead will be indirectly represented through bump
 * maps.
 **/
public abstract class Material<C extends Color<C>> {

	/** **/
	public final C diffReflect;
	
	/** **/
	public final C specReflect;
	
	/** **/
	public final C diffTransmit;
	
	/** **/
	public final C specTransmit;
	
	/**
	 *
	 **/
	public Material(C diffReflect, C specReflect, C diffTransmit, C specTransmit) {
		this.diffReflect  = diffReflect;
		this.specReflect  = specReflect;
		this.diffTransmit = diffTransmit;
		this.specTransmit = specTransmit;
	}
	
	/**
	 *
	 **/
	public abstract double diffReflectCoeff();
	
	/**
	 *
	 **/
	public abstract double specReflectCoeff();
	
	/**
	 *
	 **/
	public abstract double diffTransmitCoeff();
	
	/**
	 *
	 **/
	public abstract double specTransmitCoeff();
	
	/**
	 *
	 **/
	public abstract double ior();
}
