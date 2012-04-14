package conicio;

import conicio.util.*;

import java.util.*;

/**
 *
 **/
public abstract class Light<C extends Color<C>> {

	/**
	 *
	 **/
	public final C color;

	/**
	 *
	 **/
	public Light(C color) {
		this.color = color;
	}

	/**
	 *
	 **/
	//public abstract double falloff(double distance);

	/**
	 *
	 **/
	public abstract boolean illuminated(Vector3 point, Scene<C> scene);

	/**
	 *
	 **/
	public abstract Collection<Ray> photons();
}
