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

		this.lights.add(PointLight.create(new Vector3( 0.0,  2.0,  5.0), new RGBColor(1.0, 1.0, 1.0)));

		// add debug shapes
		this.shapes.add(  Plane.create(new Vector3( 0.0,  0.0,  0.0), 
		                               new Vector3( 0.0,  0.0,  1.0),      RGBColor.CHROME    ));
		this.shapes.add( Sphere.create(new Vector3(-0.9, -1.0,  0.1), 0.1, RGBColor.TURQUOISE ));
		this.shapes.add( Sphere.create(new Vector3( 0.2,  0.0,  0.2), 0.2, RGBColor.GLASS     ));
		this.shapes.add( Sphere.create(new Vector3( 0.8, -0.4,  0.2), 0.2, RGBColor.EMERALD   ));
		this.shapes.add( Sphere.create(new Vector3(-0.2, -0.8,  0.2), 0.2, RGBColor.TURQUOISE ));
	}
	
	@Override
	public Camera camera() {
		return new Camera(
			new Vector3( 3.0,  5.0,  1.0),
			new Vector3( 0.0,  0.0,  0.2),
			new Vector3( 0.0,  0.0,  1.0), 
			Math.PI / 6.0
		);
	}
	
	@Override
	public int xSize() { return 800; }
	@Override
	public int ySize() { return 600; }
}
