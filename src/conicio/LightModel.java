package conicio;

import conicio.shape.*;
import conicio.util.*;
import java.util.*;

public class LightModel {

	// all of this is assuming white light!
	/*
	public Color calculate(Set<Light> lights, Shape shape, Vector3 point, Vector3 toView) {
		Material material = shape.material();
		Vector3  normal   = shape.normal(point);

		Color intensity = material.ambient;

		for(Light light : lights) {
			Vector3 displacement = (light.origin).sub(point).normalize();
			Vector3 reflection   = normal.mul(displacement.dot(normal)).mul(2.0).sub(displacement).normalize();

			Color diffuse  = (material.diffuse).scale(2 * displacement.dot(normal));
			Color specular = (material.specular).scale(Math.pow(reflection.dot(toView), material.shine));

			intensity = intensity.add(diffuse).add(specular);
		}

		return intensity;
	}
	*/
}
