package other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Mandelbrot extends JFrame{
	private static final long serialVersionUID = 1L;
	public static int width = 1920/2;
	public static int height = 1080/2;
	MandelbrotPanel panel;
	ComponentListener listener;
	boolean mandelbrot = true;
	
	public Mandelbrot() {
		super("Mandelbrot viewer");
		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.white);
		panel = new MandelbrotPanel(width, height, mandelbrot);
		panel.generateImage();
		getContentPane().add(panel, BorderLayout.CENTER);
		this.setFocusable(false);
		listener = new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				int w, h;
				int oldw = width;
				int oldh = height;
				double xMaxOld = panel.xMax;
				double xMinOld = panel.xMin;
				double yMaxOld = panel.yMax;
				double yMinOld = panel.yMin;
				w = e.getComponent().getWidth();
				h = e.getComponent().getHeight();
				if (w == width && h == height) {
					return;
				}
				width = w;
				height = h;
				System.out.println("Width " + w + " Height " + h);
				getContentPane().remove(panel);
				panel = new MandelbrotPanel(w, h, mandelbrot);
				panel.xMin = xMinOld;
				panel.yMin = yMinOld;
				panel.xMax = xMaxOld + (width-oldw)*((xMaxOld-xMinOld)/(float)oldw);
				panel.yMax = yMaxOld + (height-oldh)*((yMaxOld-yMinOld)/(float)oldh);
				panel.generateImage();
				getContentPane().add(panel);
				System.out.println("HI");
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}
			
		};
		this.addComponentListener(listener);
	}
	
	public static void main(String args[]) {
		Mandelbrot m = new Mandelbrot();
		m.setVisible(true);
	}
}
