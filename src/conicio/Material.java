package conicio;

import conicio.util.*;


public class Material<C extends Color<C>> {
	public final C      ambient;
	public final C      diffuse;
	public final C      specular;
	public final double shine;
	public final double alpha;
	public final double refract;
	public final double reflect;

	public Material(C ambient, C diffuse, C specular, double shine, double alpha, double refract, double reflect) {
		this.ambient  = ambient;
		this.diffuse  = diffuse;
		this.specular = specular;
		this.shine    = shine;
		this.alpha    = alpha;
		this.refract  = refract;
		this.reflect  = reflect;
	}
}
