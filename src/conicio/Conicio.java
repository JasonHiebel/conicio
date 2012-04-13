package conicio;

import conicio.shape.*;
import conicio.util.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Conicio {

	public static void main(String[] args) {
		int xSize = 1280;
		int ySize =  800;

		Camera camera = new Camera(
			new Vector3( 3.0,  5.0,  1.0),
			new Vector3( 0.0,  0.0,  0.2),
			new Vector3( 0.0,  0.0,  1.0), 
			Math.PI / 6.0
		);

		Scene scene = new Scene();

		// add debug lights
		scene.lights.add(new PointLight(new Vector3( 0.0,  2.0,  5.0), new Color(1.0, 1.0, 1.0)));

		// add debug shapes
		scene.shapes.add(new Plane(new Vector3( 0.0,  0.0,  0.0), new Vector3( 0.0,  0.0,  1.0), Material.CHROME));
		//scene.shapes.add(new Plane(new Vector3( 0.0,  0.0,  9.0), new Vector3( 0.0,  0.0, -1.0), Material.CHROME));
		//scene.shapes.add(new Plane(new Vector3( 0.0, -4.0,  0.0), new Vector3( 0.0,  1.0,  0.0), Material.CHROME));
		//scene.shapes.add(new Plane(new Vector3( 0.0,  6.0,  0.0), new Vector3( 0.0, -1.0,  0.0), Material.CHROME));
		//scene.shapes.add(new Plane(new Vector3(-6.0,  0.0,  0.0), new Vector3( 1.0,  0.0,  0.0), Material.CHROME));
		//scene.shapes.add(new Plane(new Vector3( 6.0,  0.0,  0.0), new Vector3(-1.0,  0.0,  0.0), Material.CHROME));

		scene.shapes.add(new Sphere(new Vector3(-0.9, -1.0,  0.1), 0.1, Material.TURQUOISE));
		scene.shapes.add(new Sphere(new Vector3( 0.2,  0.0,  0.2), 0.2, Material.GLASS));
		scene.shapes.add(new Sphere(new Vector3( 0.8, -0.4,  0.2), 0.2, Material.EMERALD));
		//scene.shapes.add(new Sphere(new Vector3(-0.8, -0.4,  0.2), 0.2, Material.GLASS));
		scene.shapes.add(new Sphere(new Vector3(-0.8, -0.4,  0.2), 0.2, 
			new Material(
				new Color(1.000000, 0.000000, 0.000000),
				new Color(1.000000, 0.000000, 0.000000),
				new Color(1.000000, 0.000000, 0.000000), 
				90.0, 0.10, 1.40, 0.50
			)
		));
		scene.shapes.add(new Sphere(new Vector3(-0.2, -0.8,  0.2), 0.2, Material.TURQUOISE));
		/*scene.shapes.add(new Geometry.Intersect(
			new Material(
				new Color(1.000000, 0.000000, 0.000000),
				new Color(1.000000, 0.000000, 0.000000),
				new Color(1.000000, 0.000000, 0.000000), 
				90.0, 0.10, 1.40, 0.50
			),
			new Sphere(new Vector3( -0.65, 0.5, 0.2), 0.2, Material.TURQUOISE),
			new Plane (new Vector3( -0.65, 0.5, 0.2), new Vector3( 0.0, 1.0, 0.0), Material.TURQUOISE)
		));
		*/


		// timed rendering
		long startTime = System.nanoTime();
		try {
			String filename = String.format("img/render-%s.png", (new Date()).toString().replace(" ", "-"));
			Renderer renderer = new Renderer(scene, camera, xSize, ySize);
			renderer.run();
			BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(renderer.result(), 0, 0, xSize, ySize, null);
			ImageIO.write(image, "png", new File(filename));
		}
		catch(Exception e) {
			System.err.println(e);
		}
		long elapsedTime = System.nanoTime() - startTime;

		System.out.format("Render Time: %f (s)%n", elapsedTime / 1000000000.0);
	}
}
