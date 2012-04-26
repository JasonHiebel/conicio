package experiments;

import conicio.*;
import conicio.shapes.*;
import conicio.util.*;

/**
 *
 **/
public class BasicRGBScene extends Scene<RGBColor> {

	public BasicRGBScene() {
		super(RGBColor.FACTORY);

		this.lights.add(AreaLight.create(
			new Vector3 (  2.0, - 1.0,   9.5), 
			new Vector3 (  0.0,   2.0,   0.0), 
			new Vector3 (- 2.0,   0.0,   0.0), 
			3, 3, 
			new RGBColor( 10.0,  10.0,  10.0)
		));

		RGBMaterial white = diffuse(new RGBColor(0.5, 0.5, 0.5));
		RGBMaterial rDiff = diffuse(new RGBColor(0.5, 0.0, 0.0));
		RGBMaterial gDiff = diffuse(new RGBColor(0.0, 0.5, 0.0));
		RGBMaterial bSpec = new RGBMaterial(
			new RGBColor(0.1, 0.1, 0.4),
			new RGBColor(0.1, 0.1, 0.4),
			new RGBColor(0.0, 0.0, 0.0),
			new RGBColor(0.2, 0.2, 1.4), 1.30
		);

		this.shapes.add(Plane.create( new Vector3(  0.0,   0.0,   0.0), 
		                              new Vector3(  0.0,   0.0,   1.0),      white));
		this.shapes.add(Plane.create( new Vector3(  0.0,   0.0,  10.0), 
		                              new Vector3(  0.0,   0.0, - 1.0),      white));
		this.shapes.add(Plane.create( new Vector3(  0.0,   6.0,   0.0), 
		                              new Vector3(  0.0, - 1.0,   0.0),      rDiff));
		this.shapes.add(Plane.create( new Vector3(  0.0, - 6.0,   0.0), 
		                              new Vector3(  0.0,   1.0,   0.0),      gDiff));
		this.shapes.add(Plane.create( new Vector3(  6.0,   0.0,   0.0), 
		                              new Vector3(- 1.0,   0.0,   0.0),      white));
		this.shapes.add(Plane.create( new Vector3(-15.0,   0.0,   0.0), 
		                              new Vector3(- 1.0,   0.0,   0.0),      white));		                               
		//this.shapes.add(Sphere.create(new Vector3(  1.0,   2.5,   2.0), 2.0, bSpec));
		this.shapes.add(Sphere.create(new Vector3(-1.5, 0.0, 3.5), 2.5, RGBMaterial.GLASS));
	}
	
	public Camera camera() {
		return new Camera(
			new Vector3(-13.0,  0.0,  3.0),
			new Vector3(  0.0,  0.0,  2.0),
			new Vector3(  0.0,  0.0,  1.0), 
			Math.PI / 4.0
		);
	}
	
	public int xSize() { return 800; }
	public int ySize() { return 600; }
	
	public static RGBMaterial diffuse(RGBColor color) {
		return new RGBMaterial(color, RGBColor.BLACK, RGBColor.BLACK, RGBColor.BLACK, 1.);
	}
}
