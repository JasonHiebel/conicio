package conicio;

import conicio.shapes.*;
import conicio.util.*;

import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.*;
import java.util.*;

/**
 * Remote renderer! Render there, see it here!
 **/
public class Renderer<C extends Color<C>> implements Runnable {
	/** **/
	protected final int maxDepth;

	/** The scene description. **/
	protected final Scene<C> scene;

	/** The perspective of the rendered image. **/
	protected final Camera camera;

	/** The viewing plane pixel width. **/
	protected final int xSize;

	/** The viewing plane pixel height. **/
	protected final int ySize;

	/** **/
	//protected Map<Vector3, C> photonMap;
	protected final KDTree<Deposit> photonMap;
	
	/** **/
	protected final KDTree<Deposit> causticMap;

	/** The backing pixel matrix. **/
	protected final int[] pixels;

	// Memory mapped images
	private final MemoryImageSource source;
	private final Image buffer;

	/**
	 * Constructs a Renderer which renders an image as described by the given
	 * Scene with perspective as given by the Camera.
	 *
	 * @param scene the scene description
	 **/
	public Renderer(Scene<C> scene) { 
		this.maxDepth = 6;
	
		this.scene  = scene;
		this.camera = scene.camera();
		this.xSize  = scene.xSize();
		this.ySize  = scene.ySize();
		
		this.photonMap  = new KDTree<Deposit>();
		this.causticMap = new KDTree<Deposit>();

		/* setup image background buffer */
		this.pixels   = new int[xSize * ySize];
		this.source   = new MemoryImageSource(xSize, ySize, pixels, 0, xSize);
		this.source.setAnimated(true);
		//this.source.setFullBufferUpdates(true);
		this.buffer = Toolkit.getDefaultToolkit().createImage(source);

		/* setup the background color */
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
		//int photons = 500000;
		int photons = 200000;
	
		System.out.println("Starting Photon Mapping!");
		for(Light<C> light : scene.lights) {
			if(light instanceof PointLight) {
				// FIXME: Vary based on power of lights!
				Iterator<Ray> shooter = light.photons().iterator();
				while(photonMap.size() + causticMap.size() < photons) {
					castPhoton(shooter.next(), light.color.scale(1. / photons));
				}
			}
			else if(light instanceof AreaLight) {
				double area = 2 * (((AreaLight)light).axisX).mul(((AreaLight)light).axisY).norm();
				// FIXME: Vary based on power of lights!
				Iterator<Ray> shooter = light.photons().iterator();
				while(photonMap.size() + causticMap.size() < photons) {
					castPhoton(shooter.next(), light.color.scale(area / photons));
				}
			}
		}
		System.out.printf("Finished [General: %d, Caustic: %d]%n", photonMap.size(), causticMap.size());

		System.out.println("Starting Ray Tracing!");	
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
				C color = scene.factory.black();

				// multisampling
				for(int i = 0; i < samples; i++) {
					for(int j = 0; j < samples; j++) {
						Vector3 normal = base;
						normal = normal.sub(du.mul(x - xSize / 2.0));
						normal = normal.add(du.mul(i / (double)(samples)));
						normal = normal.add(dv.mul(y - ySize / 2.0));
						normal = normal.add(dv.mul(j / (double)(samples)));

						Ray ray = new Ray(camera.eye, normal);
						color = color.add(castRay(ray).scale(1.0 / samples / samples));
					}
				}

				pixels[y * xSize + x] = color.asRGB();
			}
		}
		System.out.println("Finished");
	}
	
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
	protected void castPhoton(Ray ray, C photon) {
		boolean specular = false;
		for(int limit = 0; limit < 6; limit++) {
			/* find ray-object intersection points */
			SortedMap<Double, Shape<C>> intersections = new TreeMap<Double, Shape<C>>();
			for(Shape<C> shape : scene.shapes) {
				double[] candidates = ray.intersect(shape);
				if(candidates.length != 0) { 
					intersections.put(candidates[0], shape); 
				}
			}
			
			if(intersections.isEmpty()) { return; }
			Shape<C> intersected  = intersections.get(intersections.firstKey());
			Vector3  intersection = ray.cast(intersections.firstKey());
			
			// restrict based on diffuse component?
			if(limit != 0 && !intersected.material.diffReflect.equals(scene.factory.black())) {
				(specular ? causticMap : photonMap).put(intersection, new Deposit(photon, intersected.normal(intersection)));
			}
			
			if(Math.random() < intersected.material.specTransmitCoeff()) {
				Map<Ray, C> innerRefractions = intersected.material.refractTo(
					ray, intersection, intersected.normal(intersection), photon
				);
				
				List<Map.Entry<Ray, C>> choices;
				
				choices = new ArrayList<Map.Entry<Ray, C>>(innerRefractions.entrySet());
				if(choices.isEmpty()) { return; }
				Map.Entry<Ray, C> innerEntry = choices.get((int)(Math.random() * choices.size()));
		
				double[] candidates = innerEntry.getKey().intersect(intersected);
				if(candidates.length != 0) {
					intersection = innerEntry.getKey().cast(candidates[0]);
					Map<Ray, C> outerRefractions = intersected.material.refractFrom(
						innerEntry.getKey(), intersection, intersected.normal(intersection), innerEntry.getValue()
					);
					
					choices = new ArrayList<Map.Entry<Ray, C>>(outerRefractions.entrySet());
					if(choices.isEmpty()) { return; }
					Map.Entry<Ray, C> outerEntry = choices.get((int)(Math.random() * choices.size()));
					
					specular = true;
					ray = outerEntry.getKey();
					
					C old = photon;
					photon = outerEntry.getValue().mask(intersected.material.specTransmit);
					photon = photon.scale(old.power() / outerEntry.getValue().power() / intersected.material.specTransmitCoeff());
				}
				else{ return; }
			}
			else {
				double diffReflect = intersected.material.diffReflectCoeff();
				double specReflect = intersected.material.specReflectCoeff();
				
				double prob = Math.random();
				if(prob <= diffReflect) {
					Vector3 normal = intersected.normal(intersection);
					
					double a = Math.atan2(normal.y, normal.x);
					double b = Math.acos(normal.z);
					
					double theta = a + Math.PI * (Math.random() - 0.5);
					double phi   = b + Math.acos(Math.random());
					
					ray = new Ray(
						intersection,
						new Vector3(
							Math.cos(theta) * Math.sin(phi),
							Math.sin(theta) * Math.sin(phi),
							Math.cos(phi)
						)
					);
					photon = photon.mask(intersected.material.diffReflect).scale(1. / diffReflect);
				}
				else if(prob <= diffReflect + specReflect) { 
					// FIXME: Verify! (Yes? Because its just asking if it IS a reflective object)
					// This can't be called if specReflect is zero, so...
					Ray reflection = new Ray(
						intersection, 
						(ray.normal).reflect(intersected.normal(intersection))
					);
					
					specular = true;
					ray = reflection;
					photon = photon.mask(intersected.material.specReflect).scale(1. / specReflect);
					// scale color by object's transmission
				}
				else { return; }
			}
		}
	}

	/**
	 *
	 **/
	protected C castRay(Ray ray) {
		return castPrimaryRay(ray, 0);
	}

	/**
	 *
	 **/
	protected C castPrimaryRay(Ray ray, int depth) {
		if(depth >= maxDepth) { return scene.factory.white(); }

		/* find ray-object intersection points */
		SortedMap<Double, Shape<C>> intersections = new TreeMap<Double, Shape<C>>();
		for(Shape<C> shape : scene.shapes) {
			double[] candidates = ray.intersect(shape);
			if(candidates.length != 0) { 
				intersections.put(candidates[0], shape); 
			}
		}

		/* assign a color to the ray */
		if(intersections.isEmpty()) { return scene.factory.black(); }
		else {
			Shape<C> intersected  = intersections.get(intersections.firstKey());
			Vector3  intersection = ray.cast(intersections.firstKey());

			C accum = scene.factory.black(); // color dynamics?
			  accum = accum.add(castReflectRay(ray, intersected, intersection, depth));
			  accum = accum.add(castRefractRay(ray, intersected, intersection, depth));
			  accum = accum.add(castShadowsRay(ray, intersected, intersection));

			return accum;
		}
	}

	/**
	 *
	 **/
	protected C castReflectRay(Ray ray, Shape<C> intersected, Vector3 intersection, int depth) {
		if(intersected.material.specReflect.equals(scene.factory.black())) { return scene.factory.black(); }

		Ray reflection = new Ray(
			intersection, 
			(ray.normal).reflect(intersected.normal(intersection))
		);
		
		C masked = intersected.material.specReflect;
		  masked = masked.mask(castPrimaryRay(reflection, depth + 1));
		return masked;
	}

	/**
	 *
	 **/
	// Model Total Internal Reflection!
	protected C castRefractRay(Ray ray, Shape<C> intersected, Vector3 intersection, int depth) {
		//double refractIndex = intersected.material.ior();
		if(intersected.material.specTransmit.equals(scene.factory.black())) { return scene.factory.black(); }

		//-----
		C color = scene.factory.black();
		Map<Ray, C> innerRefractions = intersected.material.refractTo(
			ray, intersection, intersected.normal(intersection), scene.factory.white()
		);
		
		for(Map.Entry<Ray, C> innerEntry : innerRefractions.entrySet()) {
			double[] candidates = innerEntry.getKey().intersect(intersected);
			if(candidates.length != 0) {
				intersection = innerEntry.getKey().cast(candidates[0]);
				Map<Ray, C> outerRefractions = intersected.material.refractFrom(
					innerEntry.getKey(), intersection, intersected.normal(intersection), innerEntry.getValue()
				);
				
				C outerColor = scene.factory.black();
				for(Map.Entry<Ray, C> outerEntry : outerRefractions.entrySet()) {
					outerColor = outerColor.add(castPrimaryRay(outerEntry.getKey(), depth + 1).mask(outerEntry.getValue()));
				}
				
				color = color.add(outerColor.mask(intersected.material.specTransmit));
			}
		}
		
		return color;
		//-----
		
		/*
		Vector3 inner = ray.normal.refract(intersected.normal(intersection), 1.0, refractIndex);
		//if(inner == null) { return scene.factory.white(); }
		if(inner == null) { return scene.factory.black(); }
		Ray internal = new Ray(intersection, inner);

		Ray external = null;
		double[] candidates = internal.intersect(intersected);
		if(candidates.length != 0) { 
			intersection = internal.cast(candidates[0]);

			// refract ray out of the object
			Vector3 outer = internal.normal.refract(intersected.normal(intersection).neg(), refractIndex, 1.0);
			if(outer == null) { external = internal;                     }
			else              { external = new Ray(intersection, outer); }
		}

		// beer's law?
		//return intersected.material.ambient.mask(castPrimary(external, depth + 1)).scale(1 - intersected.material.alpha);
		// model of beer's law with 1 - a as a density metric
		
		//double dist = (candidates.length != 0 ? candidates[0] : 10000.0);
		C masked = intersected.material.specTransmit;
		  masked = masked.mask(castPrimaryRay(external, depth + 1));
		//  masked.scale(Math.pow(Math.E, -(intersected.material.refract * dist));
		
		return masked;
		*/
	}

	/**
	 *
	 **/
	protected C castShadowsRay(Ray ray, Shape<C> intersected, Vector3 intersect) {
		Material<C> material = intersected.material();
		Vector3 normal       = intersected.normal(intersect);
		
		C intensity = scene.factory.black();

		for(Light<C> light : scene.lights) {
			// FIXME: abstract out in to class structure (still unsure how)
			if(light instanceof PointLight) {
				PointLight<C> pLight = (PointLight<C>)light;
				if(!pLight.illuminated(intersect, scene)) { continue; }
				
				
				Vector3 displacement = (pLight.origin).sub(intersect);
				// FIXME: unclear!
				Vector3 reflection   = normal.mul(displacement.dot(normal)).mul(2.0).sub(displacement).normalize();
				
				C diffuse  = material.diffReflect.scale(displacement.normalize().dot(normal));
				// FIXME: no specular highlight for you!
				//C specular = (material.specular).scale(Math.pow(reflection.dot((ray.normal).mul(-1)), material.shine));
				
				//intensity = intensity.add(diffuse.scale(material.alpha).add(specular).mask(pLight.color));
				//intensity = intensity.add(diffuse.add(specular).mask(pLight.color.scale(1. / displacement.normSq())));
				intensity = intensity.add(diffuse.mask(pLight.color.scale(1. / displacement.normSq())));
			}
			else if(light instanceof AreaLight) {
				AreaLight<C> aLight = (AreaLight<C>)light;
				
				// FIXME: calculated ONCE as it is invarient after creation
				double area = 2. * (aLight.axisX).mul(aLight.axisY).norm() / (aLight.sizeX * aLight.sizeY);
				
				Set<PointLight<C>> samples = new HashSet<PointLight<C>>();
				for(int x = 0; x < aLight.sizeX; x++) {
					for(int y = 0; y < aLight.sizeY; y++) {
						Vector3 point = aLight.origin;
						point = point.add(aLight.axisX.mul((1. + 2. * x) / (2. * aLight.sizeX)));
						point = point.add(aLight.axisY.mul((1. + 2. * y) / (2. * aLight.sizeY)));
						
						PointLight<C> sample = new PointLight<C>(point, aLight.color.scale(area));
						if(sample.illuminated(intersect, scene)) { samples.add(sample); }
					}
				}
				
				Vector3 average = Vector3.ZERO;
				for(PointLight<C> sample : samples) {
					Vector3 displacement = (sample.origin).sub(intersect);
					// FIXME: unclear!
					Vector3 reflection   = normal.mul(displacement.dot(normal)).mul(2.0).sub(displacement).normalize();
				
					C diffuse  = material.diffReflect.scale(displacement.normalize().dot(normal));
					//C diffuse = material.diffReflect;
					// FIXME: no specular hightlight for you!
					//C specular = (material.specular).scale(Math.pow(reflection.dot((ray.normal).mul(-1)), material.shine));
				
					//intensity = intensity.add(diffuse.scale(material.alpha).add(specular).mask(sample.color));
					//intensity = intensity.add(diffuse.add(specular).mask(sample.color.scale(1. / displacement.normSq())));
					//intensity = intensity.add(diffuse.mask(sample.color.scale(1. / displacement.normSq())));
					intensity = intensity.add(diffuse.mask(sample.color));
					average   = average.add(displacement);
				}
				intensity = intensity.scale(1. / average.mul(1. / samples.size()).normSq());
			}
		}
		
		
		double radius = 0.5;
		//double radius = 0.2;
		
		C caustic = scene.factory.black();
		Collection<Deposit> photons = photonMap.get(intersect, radius).values();
		for(Deposit deposit : photons) { 
			caustic = caustic.add(deposit.photon.scale(normal.dot(deposit.incident) / (Math.PI * radius * radius))); 
		}
		
		Collection<Deposit> caustics = causticMap.get(intersect, radius).values();
		for(Deposit deposit : caustics) {
			caustic = caustic.add(deposit.photon.scale(normal.dot(deposit.incident) / (Math.PI * radius * radius))); 
		}

		return intensity.add(caustic);
	}
	
	/**
	 *
	 **/
	public static <C extends Color<C>> Renderer<C> create(Scene<C> scene) {
		return new Renderer<C>(scene);
	}
	
	//
	private class Deposit {
		public final C       photon;
		public final Vector3 incident;
		
		public Deposit(C photon, Vector3 incident) {
			this.photon   = photon;
			this.incident = incident.normalize();
		}
	}
}
