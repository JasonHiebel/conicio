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
		for(Shape<C> shape : scene.shapes) {
			double[] candidates = ray.intersect(shape);
			if(candidates.length != 0) { 
				intersections.put(candidates[0], shape); 
			}
		}
		return intersections.headMap(origin.sub(point).norm()).isEmpty();
	}

	/**
	 *
	 **/
	public Iterable<Ray> photons() {
		return new Iterable<Ray>() {
			public Iterator<Ray> iterator() { return new PhotonGenerator(); }
		};
	}
	
	/**
	 *
	 **/
	public static <C extends Color<C>> PointLight<C> create(Vector3 origin, C color) {
		return new PointLight<C>(origin, color);
	}
	
	/**
	 *
	 **/
	protected class PhotonGenerator implements Iterator<Ray> {
	
		/**
		 *
		 **/
		protected PhotonGenerator() { }
		
		/**
		 *
		 **/
		public boolean hasNext() { 
			return true;
		}
		
		/**
		 *
		 **/
		public Ray next() {
			double theta = 2 * Math.PI * Math.random();
			double phi   = Math.acos(2 * Math.random() - 1);
			
			return new Ray(
				origin,
				new Vector3(
					Math.cos(theta) * Math.sin(phi),
					Math.sin(theta) * Math.sin(phi),
					Math.cos(phi)
				)
			);
		}
		
		/**
		 *
		 **/
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
