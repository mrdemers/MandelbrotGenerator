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

public class Mandelbrot extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	public static int width = 1920/2;
	public static int height = 1080/2;
	MandelbrotPanel panel;
	ComponentListener listener;
	
	public Mandelbrot() {
		super("Mandelbrot viewer");
		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.white);
		addKeyListener(this);
		panel = new MandelbrotPanel(width, height, true);
		getContentPane().add(panel, BorderLayout.CENTER);
		listener = new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent e) {
				int w, h;
				w = e.getComponent().getWidth();
				h = e.getComponent().getHeight();
				if (w == width && h == height) {
					return;
				}
				width = w;
				height = h;
				System.out.println("Width " + w + " Height " + h);
//				getContentPane().remove(panel);
				panel.resize(w, h);
//				panel = new MandelbrotPanel(w, h, true);
//				getContentPane().add(panel);
//				System.out.println("HI");
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
		this.addComponentListener(listener);
	}
	
	public static void main(String args[]) {
		Mandelbrot m = new Mandelbrot();
		m.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		panel.keyPressed(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
