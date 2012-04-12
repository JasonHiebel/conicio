package conicio;

import conicio.util.*;

import java.util.*;

/**
 *
 **/
public abstract class Light {

	/**
	 *
	 **/
	public final Color color;

	/**
	 *
	 **/
	public Light(Color color) {
		this.color = color;
	}

	/**
	 *
	 **/
	//public abstract double falloff(double distance);

	/**
	 *
	 **/
	public abstract boolean illuminated(Vector3 point, Scene scene);

	/**
	 *
	 **/
	public abstract Collection<Ray> photons();
}
