package conicio.shape;

import conicio.*;
import conicio.util.*;

import java.util.*;

/**
 *
 **/
public abstract class Geometry {

	/**
	 *
	 **/
	public abstract class Union extends Shape {
	
		private final Collection<Shape> shapes = new HashSet<Shape>();
	
		/**
		 *
		 **/
		public Union(Material material, Shape... shapes) {
			super(material);
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
		public abstract double[] intersect(Ray ray);
	}

	/**
	 *
	 **/
	public static class Intersect extends Shape {
	
		private final Collection<Shape> shapes = new HashSet<Shape>();
	
		/**
		 *
		 **/
		public Intersect(Material material, Shape... shapes) {
			super(material);
			Collections.addAll(this.shapes, shapes);
		}
	
		/**
	 	 *
	 	 **/
		public Vector3 normal(Vector3 point) {
			//System.out.println("" + point);
			for(Shape shape : shapes) {
				//System.out.println("    " + shape + ", " + shape.intersects(point) + ", " + shape.contains(point) + ", " + point.sub(((Sphere)shape).origin).normSq() + ", " + ((Sphere)shape).radius * ((Sphere)shape).radius);
				if(shape.intersects(point)) {
					boolean internal = true;
					for(Shape test : shapes) {
						//System.out.println("        " + test + ", " + test.intersects(point) + ", " + test.contains(point));
						if(!shape.equals(test) && !test.contains(point)) {
							internal = false;
							break;
						}
					}
					if(internal) { return shape.normal(point); }
				}
			}
			
			throw new Error();
		}

		/**
		 *
		 **/
		public boolean contains(Vector3 point) {
			for(Shape shape : shapes) {
				if(!shape.contains(point)) { return false; }
			}
			
			return true;
		}
		
		/**
		 *
		 **/
		public boolean intersects(Vector3 point) {
			for(Shape shape : shapes) {
				if(shape.intersects(point)) {
					return true;
				}
			}
			return false;
		}

		/**
		 *
		 **/
		public double[] intersect(Ray ray) {
			//System.out.println(ray);
			
			List<Double> intersections = new LinkedList<Double>();
			for(Shape shape : shapes) {
				//System.out.println("  " + shape);
			
				double[] candidates = ray.intersect(shape);
				for(double candidate : candidates) {
					//System.out.println("    " + candidate);
				
					boolean internal = true;
					for(Shape test : shapes) {
						//System.out.println("      " + test + ", " + ray.origin.add(ray.normal.mul(candidate)) + ", " + test.contains(ray.origin.add(ray.normal.mul(candidate))));
						if(!shape.equals(test) && !test.contains(ray.origin.add(ray.normal.mul(candidate)))) {
							internal = false;
							break;
						}
					}
					if(internal) { 
						intersections.add(candidate);
					}
				}
			}
			
			double[] candidates = new double[intersections.size()];
			for(int x = 0; x < intersections.size(); x++) { candidates[x] = intersections.get(x); }
			return candidates;
		}
	}
}
