package conicio.shapes;

import conicio.*;
import conicio.util.*;

/**
 *
 **/
public class Plane<C extends Color<C>> extends Shape<C> {

	/**
	 *
	 **/
	public final Vector3 origin;

	/**
	 *
	 **/
	public final Vector3 normal;

	/**
	 *
	 */
	public Plane(Vector3 origin, Vector3 normal, Material<C> material) {
		super(material);

		this.origin = origin;
		this.normal = normal.normalize();
	}

	/**
	 *
	 */
	public Vector3 normal(Vector3 point) {
		return normal;
	}

	/**
	 *
	 */
	public boolean contains(Vector3 point) {
		return point.sub(origin).dot(normal) < 0;
	}
	
	/**
	 *
	 **/
	public boolean intersects(Vector3 point) {
		return Math.abs(point.sub(origin).dot(normal)) < 0.001;
	}

	/**
	 *
	 **/
	public double[] intersect(Ray ray) {
		double s = - (ray.origin).sub(origin).dot(normal) / (ray.normal).dot(normal);

		if(s <= 0.001) { return new double[] { }; }
		return new double[] { s };
	}
	
	/**
	 *
	 **/
	public String toString() {
		return String.format("[ Plane : %s, %s ]", origin, normal);
	}
	
	/**
	 *
	 **/
	public static <C extends Color<C>> Plane<C> create(Vector3 origin, Vector3 normal, Material<C> material) {
		return new Plane<C>(origin, normal, material);
	}
}
