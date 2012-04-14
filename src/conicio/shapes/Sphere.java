package conicio.shapes;

import conicio.*;
import conicio.util.*;

/**
 *
 **/
public class Sphere<C extends Color<C>> extends Shape<C> {

	/**
	 *
	 **/
	public final Vector3 origin;

	/**
	 *
	 **/
	public final double  radius;

	/**
	 *
	 **/
	public Sphere(Vector3 origin, double radius, Material<C> material) {
		super(material);

		this.origin = origin;
		this.radius = radius;
	}

	/**
	 *
	 **/
	public Vector3 normal(Vector3 point) {
		return point.sub(origin).normalize();
	}

	/**
	 *
	 **/
	public boolean contains(Vector3 point) {
		return point.sub(origin).normSq() <= radius * radius;
	}

	/**
	 *
	 **/
	public boolean intersects(Vector3 point) {
		return Math.abs(point.sub(origin).normSq() - radius * radius) < 0.1;
	}

	/**
	 *
	 **/
	public double[] intersect(Ray ray) {
		Vector3 translate = (ray.origin).sub(origin);

		// quadratic calculations
		double b = 2 * (ray.normal).dot(translate);
		double c = translate.normSq() - (radius * radius);

		// discriminent check
		double disc = b * b - 4 * c;
		if(disc < 0) { return new double[] { }; }

		// intersection point(s)
		double s0 = (-b - Math.sqrt(disc)) / 2;
		double s1 = (-b + Math.sqrt(disc)) / 2;

		if(s1 <= 0.001) { return new double[] {    }; }
		if(s0 <= 0.001) { return new double[] { s1 }; }
		return new double[] { s0, s1 };
	}
	
	/**
	 *
	 **/
	public String toString() {
		return String.format("[ Sphere : %s, %f ]", origin, radius);
	}
	
	/**
	 *
	 **/
	public static <C extends Color<C>> Sphere<C> create(Vector3 origin, double radius, Material<C> material) {
		return new Sphere<C>(origin, radius, material);
	}
}
