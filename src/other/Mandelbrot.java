package other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Mandelbrot extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	public static int width = 1920/2;
	public static int height = 1080/2;
	MandelbrotPanel panel;
	
	public Mandelbrot() {
		super("Mandelbrot viewer");
		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.white);
		addKeyListener(this);
		panel = new MandelbrotPanel(width, height);
		getContentPane().add(panel, BorderLayout.CENTER);
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
