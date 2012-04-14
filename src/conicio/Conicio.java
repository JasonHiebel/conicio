package conicio;

import conicio.shapes.*;
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

		Scene<RGBColor> scene = new Scene<RGBColor>(RGBColor.FACTORY);

		// add debug lights
		scene.lights.add(PointLight.create(new Vector3( 0.0,  2.0,  5.0), new RGBColor(1.0, 1.0, 1.0)));

		// add debug shapes
		scene.shapes.add( Plane.create(new Vector3( 0.0,  0.0,  0.0), new Vector3( 0.0,  0.0,  1.0), RGBColor.CHROME));
		scene.shapes.add(Sphere.create(new Vector3(-0.9, -1.0,  0.1), 0.1, RGBColor.TURQUOISE));
		scene.shapes.add(Sphere.create(new Vector3( 0.2,  0.0,  0.2), 0.2, RGBColor.GLASS));
		scene.shapes.add(Sphere.create(new Vector3( 0.8, -0.4,  0.2), 0.2, RGBColor.EMERALD));
		scene.shapes.add(Sphere.create(new Vector3(-0.2, -0.8,  0.2), 0.2, RGBColor.TURQUOISE));

		// timed rendering
		long startTime = System.nanoTime();
		try {
			String filename = String.format("img/render-%s.png", (new Date()).toString().replace(" ", "-"));
			Renderer<?> renderer = new Renderer<RGBColor>(scene, camera, xSize, ySize);
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
