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
	public abstract Iterable<Ray> photons();
}
