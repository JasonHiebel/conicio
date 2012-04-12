package conicio;

import conicio.shape.*;
import conicio.util.*;

import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import conicio.util.Color;

/**
 *
 **/
public class Visualizer implements Runnable {
	
	protected final JFrame frame;
	
	/**
	 *
	 **/
	public Visualizer(Renderer renderer) {
		frame = new JFrame("Conicio Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		frame.add(new GraphicsPane(renderer));
		frame.validate();
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 *
	 **/
	public void run() {
		while(true) {
			try {
				frame.repaint();
				Thread.sleep(50);
			}
			catch(InterruptedException e) {}
		}
	}
	
	/**
	 *
	 **/
	public class GraphicsPane extends Component {
		protected final Renderer renderer;
		
		/**
		 *
		 **/
		public GraphicsPane(Renderer renderer) {
			this.renderer = renderer;
			
			Image result = renderer.result();
			this.setPreferredSize(new Dimension(result.getWidth(null), result.getHeight(null)));
		}
		
		/**
		 *
		 **/
		public void paint(Graphics g) {
			g.drawImage(renderer.result(), 0, 0, null);
		}
	}
	
	public static void main(String[] args) {
		int xSize = 800;
		int ySize = 600;

		Camera camera = new Camera(
			new Vector3( 3.0,  5.0,  1.0),
			new Vector3( 0.0,  0.0,  0.2),
			new Vector3( 0.0,  0.0,  1.0), 
			Math.PI / 6.0
		);

		Scene scene = new Scene();

		scene.lights.add(new PointLight(new Vector3( 0.0,  2.0,  5.0), new Color(1.0, 1.0, 1.0)));

		scene.shapes.add(new Plane(new Vector3( 0.0,  0.0,  0.0), new Vector3( 0.0,  0.0,  1.0), Material.CHROME));
		scene.shapes.add(new Sphere(new Vector3(-0.9, -1.0,  0.1), 0.1, Material.TURQUOISE));
		scene.shapes.add(new Sphere(new Vector3( 0.2,  0.0,  0.2), 0.2, Material.EMERALD));
		scene.shapes.add(new Sphere(new Vector3( 0.8, -0.4,  0.2), 0.2, Material.EMERALD));
		scene.shapes.add(new Sphere(new Vector3(-0.8, -0.4,  0.2), 0.2, 
			new Material(
				new Color(1.000000, 0.000000, 0.000000),
				new Color(1.000000, 0.000000, 0.000000),
				new Color(1.000000, 0.000000, 0.000000), 
				90.0, 0.10, 1.40, 0.50
			)
		));
		scene.shapes.add(new Sphere(new Vector3(-0.2, -0.8,  0.2), 0.2, Material.TURQUOISE));


		try {
			String filename = String.format("render-%s.png", (new Date()).toString().replace(" ", "-"));
			Renderer renderer = new Renderer(scene, camera, xSize, ySize);
			
			Visualizer visualizer = new Visualizer(renderer);
			new Thread(visualizer).start();
			
			renderer.run();
			//BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
			//image.getGraphics().drawImage(renderer.result(), 0, 0, xSize, ySize, null);
			//ImageIO.write(image, "png", new File(filename));
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}
}