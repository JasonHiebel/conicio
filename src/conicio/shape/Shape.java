package conicio.shape;

import conicio.*;
import conicio.util.*;

/**
 *
 **/
public abstract class Shape {

	/**
	 *
	 **/
	public final Material material;

	/**
	 *
	 **/
	protected Shape(Material material) {
		this.material = material;
	}

	/**
	 *
	 **/
	public abstract Vector3 normal(Vector3 point);

	/**
	 *
	 **/
	public abstract boolean contains(Vector3 point);

	/**
	 *
	 **/
	public abstract boolean intersects(Vector3 point);

	/**
	 *
	 **/
	public abstract double[] intersect(Ray ray);

	/**
	 *
	 **/
	public boolean intersects(Ray ray) {
		return (0 < intersect(ray).length);
	}

	/**
	 *
	 **/
	public Material material() {
		return material;
	}
}
