package conicio.scenes;

import conicio.*;
import conicio.shapes.*;
import conicio.util.*;

/**
 *
 **/
public class DefaultScene extends Scene<RGBColor> {

	public DefaultScene() {
		super(RGBColor.FACTORY);

		this.lights.add(AreaLight.create(
			new Vector3( 1.0, -3.0,  9.5), 
			new Vector3( 0.0,  2.0,  0.0), 
			new Vector3(-2.0,  0.0,  0.0), 
			2, 2, 
			new RGBColor(4.0, 4.0, 4.0)
		));

		// add debug shapes
		this.shapes.add(Plane.create(new Vector3(  0.0,   0.0,   0.0), 
		                             new Vector3(  0.0,   0.0,   1.0), RGBMaterial.CHROME));
		this.shapes.add(Plane.create(new Vector3(  0.0,   0.0,  10.0), 
		                             new Vector3(  0.0,   0.0, - 1.0), RGBMaterial.CHROME));
		this.shapes.add(Plane.create(new Vector3(  0.0,   6.0,   0.0), 
		                             new Vector3(  0.0, - 1.0,   0.0), RGBMaterial.CHROME));
		this.shapes.add(Plane.create(new Vector3(  0.0, - 6.0,   0.0), 
		                             new Vector3(  0.0,   1.0,   0.0), RGBMaterial.CHROME));
		this.shapes.add(Plane.create(new Vector3(  6.0,   0.0,   0.0), 
		                             new Vector3(- 1.0,   0.0,   0.0), RGBMaterial.CHROME));
		this.shapes.add(Plane.create(new Vector3(-15.0,   0.0,   0.0), 
		                             new Vector3(- 1.0,   0.0,   0.0), RGBMaterial.CHROME));
		                               
		this.shapes.add( Sphere.create(new Vector3( 0.0,  0.0,  2.0), 2.0, RGBMaterial.GLASS));
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
}
