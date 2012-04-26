package experiments;

import conicio.*;
import conicio.shapes.*;
import conicio.util.*;

/**
 *
 **/
public class SlotAbberationScene extends Scene<SlotColor> {

	public SlotAbberationScene() {
		super(SlotColor.FACTORY);

		this.lights.add(AreaLight.create(
			new Vector3 (  2.0, - 1.0,   9.5), 
			new Vector3 (  0.0,   2.0,   0.0), 
			new Vector3 (- 2.0,   0.0,   0.0), 
			1, 1, 
			new SlotColor(new Range(380, 780, 10))
		));

		SlotMaterial white = diffuse(new SlotColor(new Range(380, 780, 0.25)));
		SlotMaterial rDiff = diffuse(new SlotColor(new Range(650, 780, 0.3)));
		SlotMaterial gDiff = diffuse(new SlotColor(new Range(500, 600, 0.3)));

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
		//this.shapes.add(Sphere.create(new Vector3(- 2.5, - 2.0,   1.3), 1.3, SlotMaterial.GLASS));
		this.shapes.add(Sphere.create(new Vector3(-8.5, 0.0, 3.0), 1.5, SlotMaterial.GLASS));
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
