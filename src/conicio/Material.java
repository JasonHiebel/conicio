package conicio;

import conicio.util.*;


public class Material {
	public final Color  ambient;
	public final Color  diffuse;
	public final Color  specular;
	public final double shine;
	public final double alpha;
	public final double refract;
	public final double reflect;

	public Material(Color ambient, Color diffuse, Color specular, double shine, double alpha, double refract, double reflect) {
		this.ambient  = ambient;
		this.diffuse  = diffuse;
		this.specular = specular;
		this.shine    = shine;
		this.alpha    = alpha;
		this.refract  = refract;
		this.reflect  = reflect;
	}

	/**
	 * Materials found at: 
	 * http://www.opengl.org/resources/code/samples/advanced/advanced97/notes/node84.html#SECTION000105200000000000000
	 */
	public static final Material CHROME = new Material(
		new Color(0.250000, 0.250000, 0.250000),
		new Color(0.400000, 0.400000, 0.400000),
		new Color(0.774597, 0.774597, 0.774597), 76.8, 1.00, 0.00, 0.20);
	public static final Material EMERALD = new Material(
		new Color(0.021500, 0.174500, 0.021500),
		new Color(0.075680, 0.614240, 0.075680),
		new Color(0.633000, 0.727811, 0.633000), 76.8, 0.55, 1.56, 0.30); 
	public static final Material TURQUOISE = new Material(
		new Color(0.100000, 0.187250, 0.174500),
		new Color(0.396000, 0.741510, 0.691020),
		new Color(0.297254, 0.308290, 0.306678), 12.8, 0.80, 1.61, 0.10);
	public static final Material GLASS = new Material(
		new Color(1.000000, 1.000000, 1.000000),
		new Color(1.000000, 1.000000, 1.000000),
		new Color(1.000000, 1.000000, 1.000000), 90.0, 0.00, 1.40, 0.30);
}
