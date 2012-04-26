package experiments;

import conicio.*;
import conicio.shapes.*;
import conicio.util.*;

/**
 *
 **/
public class BasicSlotScene extends Scene<SlotColor> {

	public BasicSlotScene() {
		super(SlotColor.FACTORY);

		this.lights.add(AreaLight.create(
			new Vector3 (  5.0, - 1.0,   9.5), 
			new Vector3 (  0.0,   2.0,   0.0), 
			new Vector3 (- 2.0,   0.0,   0.0), 
			2, 2, 
			new SlotColor(new Range(380, 780, 20. / SlotColor.SAMPLES))
		));

		SlotMaterial white = diffuse(new SlotColor(new Range(380, 780, 0.25)));
		SlotMaterial rDiff = diffuse(new SlotColor(new Range(650, 780, 0.3)));
		SlotMaterial gDiff = diffuse(new SlotColor(new Range(500, 600, 0.3)));
		//SlotMaterial bSpec = new RGBMaterial(
		//	new RGBColor(0.1, 0.1, 0.4),
		//	new RGBColor(0.1, 0.1, 0.4),
		//	new RGBColor(0.0, 0.0, 0.0),
		//	new RGBColor(0.2, 0.2, 1.4), 1.30
		//);

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
		//this.shapes.add(Sphere.create(new Vector3(-1.5, 0.0, 3.1), 2.5, SlotMaterial.GLASS));
		this.shapes.add(Sphere.create(new Vector3(0.0, 1.5, 3.1), 2., SlotMaterial.GLASS));
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
	
	public static SlotMaterial diffuse(SlotColor color) {
		return new SlotMaterial(color, SlotColor.BLACK, SlotColor.BLACK, SlotColor.BLACK, 1.);
	}
}
