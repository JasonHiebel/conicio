package conicio;

import conicio.shapes.*;
import conicio.util.*;

import java.util.*;

/**
 *
 **/
public class PointLight<C extends Color<C>> extends Light<C> {

	/**
	 *
	 **/
	public final Vector3 origin;

	/**
	 *
	 **/
	public PointLight(Vector3 origin, C color) {
		super(color);
		this.origin = origin;
	}

	/**
	 *
	 **/
	public boolean illuminated(Vector3 point, Scene<C> scene) {
		Ray ray = new Ray(point, origin.sub(point).normalize());
		
		SortedMap<Double, Shape<C>> intersections = scene.collisions(ray);
		return intersections.headMap(origin.sub(point).norm()).isEmpty();
	}

	/**
	 *
	 **/
	public Collection<Ray> photons() {
		Collection<Ray> photons = new HashSet<Ray>();

		int samples = 1000;
		for(int s = 0; s < samples; s++) {
			// generate spherical coordinates
			double theta =     Math.PI * (Math.random() - 0.5);
			double phi   = 2 * Math.PI *  Math.random();

			// convert to rectangular coordinates and construct ray
			Vector3 normal = new Vector3(Math.sin(theta) * Math.cos(phi), Math.sin(theta) * Math.sin(phi), Math.cos(phi));
			photons.add(new Ray(origin, normal));
		}

		return photons;
	}
	
	/**
	 *
	 **/
	/**
	 *
	 **/
	public static <C extends Color<C>> PointLight<C> create(Vector3 origin, C color) {
		return new PointLight<C>(origin, color);
	}
}
