package conicio;

import conicio.shapes.*;
import conicio.util.*;

/**
 *
 **/
public class Ray {

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
	 **/
	public Ray(Vector3 origin, Vector3 normal) {
		this.origin = origin;
		this.normal = normal.normalize();
	}

	/**
	 *
	 **/
	public final Vector3 cast(double s) {
		return origin.add(normal.mul(s));
	}

	/**
	 *
	 **/
	public final double[] intersect(Shape<?> shape) {
		return shape.intersect(this);
	}

	/**
	 *
	 **/
	public final boolean intersects(Shape<?> shape) {
		return shape.intersects(this);
	}
}
