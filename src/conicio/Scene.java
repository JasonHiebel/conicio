package conicio;

import conicio.shapes.*;
import conicio.util.*;
import java.util.*;

/**
 *
 **/
public abstract class Scene<C extends Color<C>> {

	/** **/
	public final Collection<Shape<C>> shapes;

	/** **/
	public final Collection<Light<C>> lights;

	/** The color factory, for the specification of default colors **/
	protected final ColorFactory<C> factory;

	/**
	 *
	 **/
	protected Scene(Collection<Shape<C>> shapes, Collection<Light<C>> lights, ColorFactory<C> factory) {
		this.shapes  = shapes;
		this.lights  = lights;
		this.factory = factory;
	}

	/**
	 *
	 **/
	public Scene(ColorFactory<C> factory) {
		this(new HashSet<Shape<C>>(), new HashSet<Light<C>>(), factory);
	}

	/**
	 *
	 **/
	public SortedMap<Double, Shape<C>> collisions(Ray ray) {
		SortedMap<Double, Shape<C>> intersections = new TreeMap<Double, Shape<C>>();
		for(Shape<C> shape : shapes) {
			double[] candidates = ray.intersect(shape);
			if(candidates.length != 0) {
				intersections.put(candidates[0], shape);
			}
		}
		
		return intersections;
	}
	
	/**
	 *
	 **/
	public abstract int xSize();
	
	/**
	 *
	 **/
	public abstract int ySize();
}


