package conicio;

import conicio.shape.*;
import conicio.util.*;
import java.util.*;

/**
 *
 **/
public class Scene {

	/**
	 *
	 **/
	public final Collection<Shape> shapes;

	/**
	 *
	 **/
	public final Collection<Light> lights;

	/**
	 *
	 **/
	public final LightModel lightModel;

	/**
	 *
	 **/
	protected Scene(LightModel lightModel, Collection<Shape> shapes, Collection<Light> lights) {
		this.lightModel = lightModel;
		this.shapes     = shapes;
		this.lights     = lights;
	}

	/**
	 *
	 **/
	public Scene(LightModel lightModel) {
		this(lightModel, new HashSet<Shape>(), new HashSet<Light>());
	}

	/**
	 *
	 **/
	public Scene() {
		this(new LightModel(), new HashSet<Shape>(), new HashSet<Light>());
	}

	/**
	 *
	 **/
	public SortedMap<Double, Shape> collisions(Ray ray) {
		SortedMap<Double, Shape> intersections = new TreeMap<Double, Shape>();
		for(Shape shape : shapes) {
			double[] candidates = ray.intersect(shape);
			if(candidates.length != 0) {
				intersections.put(candidates[0], shape);
			}
		}
		
		return intersections;
	}
}


