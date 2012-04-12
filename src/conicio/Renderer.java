package conicio;

import conicio.shape.*;
import conicio.util.*;

import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.*;
import java.util.*;

/**
 *
 **/
public class Renderer implements Runnable {
	/** The scene description. **/
	protected final Scene scene;

	/** The perspective of the rendered image. **/
	protected final Camera camera;

	/** The viewing plane pixel width. **/
	protected final int xSize;

	/** The viewing plane pixel height. **/
	protected final int ySize;

	/** The backing pixel matrix. **/
	protected final int[] pixels;

	// Memory mapped images
	private final MemoryImageSource source;
	private final Image buffer;

	/**
	 * Constructs a Renderer which renders an image as described by the given
	 * Scene with perspective as given by the Camera.
	 *
	 * @param scene  the scene description
	 * @param camera the perspective of the rendered image
	 * @param xSize  the viewing plane pixel width
	 * @param ySize  the viewing plane pixel height
	 **/
	public Renderer(Scene scene, Camera camera, int xSize, int ySize) { 
		this.scene  = scene;
		this.camera = camera;
		this.xSize  = xSize;
		this.ySize  = ySize;

		// setup image background buffer
		this.pixels = new int[xSize * ySize];
		this.source = new MemoryImageSource(xSize, ySize, pixels, 0, xSize);
		this.source.setAnimated(true);
		//this.source.setFullBufferUpdates(true);
		this.buffer = Toolkit.getDefaultToolkit().createImage(source);

		// setup the background color
		final int background = 0xFF000000;
		for(int y = 0; y < ySize; y++) {
			for(int x = 0; x < xSize; x++) {
				pixels[y * xSize + x] = background;
			}
		}
	}

	/**
	 *
	 **/
	public void run() {
		// clean this up?
		Vector3 direction = camera.center.sub(camera.eye);
		Vector3 up = camera.up;
		Vector3 du = direction.mul(up).neg().normalize();
		Vector3 dv = direction.mul(du).neg().normalize();

		double fovAdjust = xSize / (2.0 * Math.tan(camera.fov / 2.0));
		Vector3 base = direction.normalize().mul(fovAdjust);

		int samples = 1; // becomes multisampler
		for(int y = 0; y < ySize; y++) {
			for(int x = 0; x < xSize; x++) {
				Color color = new Color(0.0, 0.0, 0.0);

				// multisampling
				Vector3 normal = base.sub(du.mul(x - xSize / 2.0)).add(dv.mul(y - ySize / 2.0));

				Ray ray = new Ray(camera.eye, normal);
				color = color.add(castRay(ray).scale(1.0 / samples));

				pixels[y * xSize + x] = color.asRGB();
			}
		}
	}

	/**
	 *
	 **/
	/*
	protected Set<Vector3> photonMap() {
		// As the scene is never mutated during the generation of the photon map, this process may be parallelized.
		// Note however that each thread should probably have its own set of points and they should be merged after the parallel portion.
		Set<Vector3> photonMap = new HashSet<Vector3>();
		for(Light light : scene.lights) {
			for(Ray photon : light.photons()) {
				
			}
		}

		return photonMap;
	}
	*/

	/**
	 *
	 **/
	public Image result() {
		source.newPixels();
		return buffer;
	}

	/**
	 *
	 **/
	/*
	protected void castPrimaryPhoton(Set<Vector3> photonMap, Ray ray, Color color, int depth) {
		if(depth >= 3) { return; } // off in to nothingness

		SortedMap<Double, Shape> intersections = new TreeMap<Double, Shape>();
		for(Shape shape : shapes) {
			double[] candidates = ray.intersect(shape);
			if(candidates.length != 0) {
				intersections.put(candidates[0], shape);
			}
		}

		if(intersections.isEmpty()) { return; } // off in to nothingness again
		else {
			Shape   intersected = intersections.get(intersections.firstKey());
			Vector3 intersect   = ray.cast(intersections.firstKey());

			if(intersected.material.alpha   < 1.00) { castRefractPhoton(photonMap, new Ray(intersect, ray.normal), color, depth); }
			if(intersected.material.reflect > 0.00) { castReflectPhoton(photonMap, new Ray(intersect, ray.normal), color, depth); }
		}
	}
	*/



	/**
	 *
	 **/
	public final Color castRay(Ray ray) {
		return castPrimary(ray, 0);
	}

	/**
	 *
	 **/
	protected Color castPrimary(Ray ray, int depth) {
		// variable maxDepth
		if(depth >= 3) { return new Color(1.0, 1.0, 1.0); }

		// parametric parameters for intersection
		SortedMap<Double, Shape> intersections = new TreeMap<Double, Shape>();
		for(Shape shape : scene.shapes) {
			double[] candidates = ray.intersect(shape);
			if(candidates.length != 0) {
				intersections.put(candidates[0], shape);
			}
		}

		// assign a color to the ray
		if(intersections.isEmpty()) { return new Color(0.0, 0.0, 0.0); }
		else {
			Shape   intersected = intersections.get(intersections.firstKey());
			Vector3 intersect   = ray.cast(intersections.firstKey());

			//if(intersected instanceof Sphere) { System.out.println(intersected.material.diffuse); }

			Color accum = new Color(0.0, 0.0, 0.0); // color dynamics?
			accum = accum.add(castReflect(ray.normal, intersected, intersect, depth));
			accum = accum.add(castRefract(ray.normal, intersected, intersect, depth));
			accum = accum.add(castShadows(ray.normal, intersected, intersect));

			return accum;
		}
	}

	/**
	 *
	 **/
	protected Color castReflect(Vector3 direction, Shape intersected, Vector3 intersect, int depth) {
		double reflectIndex = intersected.material.reflect;
		if(reflectIndex <= 0.0) { return new Color(0.0, 0.0, 0.0); }

		Ray reflection = new Ray(intersect, direction.reflect(intersected.normal(intersect)));
		return (intersected.material.ambient).mask(castPrimary(reflection, depth + 1)).scale(reflectIndex);
	}

	/**
	 *
	 **/
	protected Color castRefract(Vector3 direction, Shape intersected, Vector3 intersect, int depth) {
		double refractIndex = intersected.material.refract;
		if(refractIndex <= 0.0) { return new Color(0.0, 0.0, 0.0); }

		Vector3 inner = direction.normalize().refract(intersected.normal(intersect), 1.0, refractIndex);
		if(inner == null) { return new Color(1.0, 0.0, 1.0); }
		Ray internal = new Ray(intersect, inner.normalize());

		Ray external = internal;
		double[] candidates = internal.intersect(intersected);
		if(candidates.length != 0) { 
			intersect = internal.cast(candidates[0]);

			Vector3 outer = inner.normalize().refract(intersected.normal(intersect).mul(-1), refractIndex, 1.0);
			if(outer == null) { outer = inner; }
			external = new Ray(intersect, outer);
		}

		// beer's law?
		//return intersected.material.ambient.mask(castPrimary(external, depth + 1)).scale(1 - intersected.material.alpha);

		// model of beer's law with 1 - a as a density metric
		double dist = (candidates.length != 0 ? candidates[0] : 10000.0);
		return intersected.material.ambient.mask(castPrimary(external, depth + 1)).scale(Math.pow(Math.E, -(intersected.material.refract * dist)));
	}

	/**
	 *
	 **/
	protected Color castShadows(Vector3 direction, Shape intersected, Vector3 intersect) {
		//Set<Light> active = new HashSet<Light>();
		//for(Light light : scene.lights) {
		//	if(light.illuminated(intersect, scene)) { active.add(light); }
		//}
		
		//Material material = intersected.material();
		//Vector3  normal   = intersected.normal(intersect);
		
		//Color intensity = Color.BLACK;
		//for(Light light : active) {
		//
		//}
	
	
	
		Set<Light> active = new HashSet<Light>();

		for(Light light : scene.lights) {
			if(light.illuminated(intersect, scene)) { active.add(light); }
		}

		Material material = intersected.material();
		Vector3 normal    = intersected.normal(intersect);
		
		Color intensity = new Color(0.0, 0.0, 0.0);

		for(Light light : active) {
			Vector3 displacement = (((PointLight)light).origin).sub(intersect).normalize();
			Vector3 reflection   = normal.mul(displacement.dot(normal)).mul(2.0).sub(displacement).normalize();

			Color diffuse  = (material.diffuse ).scale(2 * displacement.dot(normal));
			Color specular = (material.specular).scale(Math.pow(reflection.dot(direction.mul(-1)), material.shine));

			//specular.scale(material.reflect)
			intensity = intensity.add(diffuse.scale(material.alpha).add(specular).mask(light.color));
		}
		
		return intensity;
	}
}
