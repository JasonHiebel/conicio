package conicio;

import conicio.shapes.*;
import conicio.util.*;

import java.util.*;

/**
 *
 **/
public class AreaLight<C extends Color<C>> extends Light<C> {
	/**
	 *
	 **/
	public final Vector3 origin;

	/**
	 *
	 **/
	public final Vector3 axisX;
	
	/**
	 *
	 **/
	public final Vector3 axisY;
	
	/**
	 *
	 **/
	public final int sizeX;
	
	/**
	 *
	 **/
	public final int sizeY;

	/**
	 *
	 **/
	public AreaLight(Vector3 origin, Vector3 axisX, Vector3 axisY, int sizeX, int sizeY, C color) {
		super(color);
		this.origin = origin;
		this.axisX  = axisX;
		this.axisY  = axisY;
		this.sizeX  = sizeX;
		this.sizeY  = sizeY;
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
	public static <C extends Color<C>> AreaLight<C> create(Vector3 origin, Vector3 axisX, Vector3 axisY, int sizeX, int sizeY, C color) {
		return new AreaLight<C>(origin, axisX, axisY, sizeX, sizeY, color);
	}
	
	/**
	 *
	 **/
	protected class PhotonGenerator implements Iterator<Ray> {
		private final Vector3 normal;
		
		/**
		 *
		 **/
		protected PhotonGenerator() {
			this.normal  = axisX.mul(axisY);
		}
		
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
			double x = Math.random();
			double y = Math.random();
			
			double theta = 2 * Math.PI * Math.random();
			double phi   = Math.random() - 1;
			
			return new Ray(
				origin.add(axisX.mul(x)).add(axisY.mul(y)),
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
