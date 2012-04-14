package conicio;

import conicio.shapes.*;
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
		public static final long serialVersionUID = 1L;
	
		protected final Renderer<?> renderer;
		
		/**
		 *
		 **/
		public GraphicsPane(Renderer<?> renderer) {
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
}