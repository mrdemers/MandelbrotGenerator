package other;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class MandelbrotPanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener, MouseWheelListener{
	private static final long serialVersionUID = 1L;
	public int width, height;
	public double xMin = -1;
	public double xMax = 1.5;
	public double yMin = -1;
	public double yMax = 1;
	public BufferedImage image;
	public int pixels[];
	public double iters[];
	public int maxIterations = 10;
	public int iterations;
	public double juliaA = 1.3, juliaB = .73;
	public Color color1, color2, color3;
	
	//Mouse event variables
	int mouseXStart, mouseYStart, mouseXEnd, mouseYEnd;
	int rectWidth, rectHeight;
	int mouseXCurrent, mouseYCurrent;
	boolean draggingMouse = false;
	ConstantsFrame of;
	
	public MandelbrotPanel(int width, int height) {
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pixels = new int[width * height];
		iters = new double[width * height];
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		addKeyListener(this);
		of = new ConstantsFrame();
		of.setVisible(true);
		color1 = new Color(55, 89, 180);
		color2 = new Color(155, 189, 10);
		color3 = new Color(0,0,0);
		generateImage();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				image.setRGB(j, i, pixels[j + i * width]);
			}
		}
		g.drawImage(image, 0, 0, width, height, null);
		if (draggingMouse) {
			g.setColor(Color.white);
			
			int x1 = mouseXStart;
			int y1 = mouseYStart;
			int w = rectWidth;
			int h = rectHeight;
			if (w < 0) {
				x1 += w;
				w = -w;
			}
			if (h < 0) {
				y1 += h;
				h = -h;
			}
			g.drawRect(x1, y1, w, h);
		}
//		g.setColor(Color.white);
//		g.drawString("" + iters[mouseXCurrent + mouseYCurrent * width], mouseXCurrent, mouseYCurrent);
	}
	
	public void generateImage() {
		//System.out.println(maxIterations);
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xffffffff;
		}
		double highestEscape = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				
				double a = xMin + x * (xMax - xMin) / width;
				double b = yMin + y * (yMax - yMin) / height;
				double escapeSpeed = escapeSpeedJulia(a,b);
				if (escapeSpeed > highestEscape) highestEscape = escapeSpeed;
				if (iterations == maxIterations) {
//					int num = (int)(escapeSpeed* 255);
//					if (escapeSpeed > 1) {
//						num = (int)(escapeSpeed/highestEscape*255);
//					}
//					if (num < 0) num = -num;
//					if (num > 255) num = 255;
//					int r = 255-num;
//					int g = 255-num;
//					int bb = 255-num;
//					pixels[x + y * width] = new Color(r, g, bb).getRGB();
					pixels[x+y*width] = color3.getRGB();
				} else {
					//Part of the code that figures out colors
						//int iter = maxIterations - iterations;
					
					float colPercent = (float)iterations/maxIterations;
					float r1 = color1.getRed(), g1 = color1.getGreen(), b1 = color1.getBlue();
					float r2 = color2.getRed(), g2 = color2.getGreen(), b2 = color2.getBlue();
					int colRed = (int)(r1*colPercent+r2*(1-colPercent));
					int colGreen = (int)(g1*colPercent+g2*(1-colPercent));
					int colBlue = (int)(b1*colPercent+b2*(1-colPercent));
					float[] vals = Color.RGBtoHSB(colRed, colGreen, colBlue, null);
					//vals[2] = (float)Math.sqrt(colPercent);
					pixels[x + y * width] = Color.HSBtoRGB(vals[0], vals[1], vals[2]); 
//					pixels[x + y * width] = Color.HSBtoRGB(col, 1, 1.0f);
//					int color = (int)(escapeSpeed);
//					pixels[x + y * width] = new Color(color, 0, 0).getRGB();
				}
				iters[x + y * width] = escapeSpeed;
			}
		}
		of.juliaABox.setText("" + juliaA);
		of.juliaBBox.setText("" + juliaB);
		repaint();
	}
	
	public double escapeSpeedMandlebrot(double a, double b) {
		iterations = 0;
		double x = 0.0;
		double y = 0.0;
		while (x * x + y * y <= 4) {
			double xNew = x * x - y * y + a;
			double yNew = 2.0 * x * y + b;
			x = xNew;
			y = yNew;
			iterations++;
			if (iterations == maxIterations) {
				return -1;
			}
		}
		return x * x + y * y;
	}
	
	public double escapeSpeedJulia(double a, double b) {
		iterations = 0;
		double x = a;
		double y = b;
		while (x*x+y*y <= 4) {
			double xx = x - x*x+y*y;
			double yy = 2*x*y-y;
			x = juliaA * xx + juliaB * yy;
			y = juliaB * xx - juliaA * yy;
			iterations++;
			if (iterations == maxIterations) break;
		}
		return x*x+y*y;
		
//		double X = a;
//		double Y = b;
//		double A = juliaA;
//		double B = juliaB;
//		double xFactor = 0.0;
//		double yFactor = 0.0;
//		double xSquare = 0, ySquare = 0;
//		int color = 0;
//		for (color = 0; color <= maxIterations - 1; color++) {
//			xFactor = X - X * X + Y * Y;
//			yFactor = 2 * X * Y - Y;
//			X = A * xFactor + B * yFactor;
//			Y = B * xFactor - A * yFactor;
//			xSquare = X * X;
//			xSquare = Y * Y;
//			iterations++;
//			if ((xSquare + ySquare) > 4) return color;
//		}
//		return -1;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			double xCenter = xMin + e.getX() * (xMax - xMin) / width;
			double yCenter = yMin + e.getY() * (yMax - yMin) / height;
			double zoomAmount = Math.abs(1.2 * xMin);
			xMin -= zoomAmount;
			xMax += zoomAmount;
			yMin -= zoomAmount;
			yMax += zoomAmount;
			double distX = xMax - xMin;
			double distY = yMax - yMin;
			xMin = xCenter - distX/2;
			xMax = xCenter + distX/2;
			yMin = yCenter - distY/2;
			yMax = yCenter + distY/2;
			generateImage();
		}
//		if (e.getButton() == MouseEvent.BUTTON1){
//			double xCenter = xMin + e.getX() * (xMax - xMin) / width;
//			double yCenter = yMin + e.getY() * (yMax - yMin) / height;
//			double zoomAmount = Math.abs(.2 * xMin);
//			xMin += zoomAmount;
//			xMax -= zoomAmount;
//			yMin += zoomAmount;
//			yMax -= zoomAmount;
//			double distX = xMax - xMin;
//			double distY = yMax - yMin;
//			xMin = xCenter - distX/2;
//			xMax = xCenter + distX/2;
//			yMin = yCenter - distY/2;
//			yMax = yCenter + distY/2;
//			generateImage();
//		}
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		mouseXStart = e.getX();
		mouseYStart = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		if (draggingMouse) {
			draggingMouse = false;
			int x1 = mouseXStart;
			int y1 = mouseYStart;
			int w = rectWidth;
			int h = rectHeight;
			if (w < 0) {
				x1 += w;
				w = -w;
			}
			if (h < 0) {
				y1 += h;
				h = -h;
			}
			double temp = xMin + x1 * (xMax - xMin) / width;
			xMax = xMin + (x1 + w) * (xMax - xMin) / width;
			xMin = temp;
			temp = yMin + y1 * (yMax - yMin) / height;
			yMax = yMin + (y1 + h) * (yMax - yMin) / height;
			yMin = temp;
			generateImage();
		}
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if (Math.abs(mouseXStart - x) > 20 || Math.abs(mouseYStart - y) > 20) 
			draggingMouse = true;
		else
			draggingMouse = false;
		if (Math.abs(mouseXStart - x) > Math.abs(mouseYStart - y)) {
			rectWidth = x - mouseXStart;
			rectHeight = rectWidth * height / width;
			if (y < mouseYStart) rectHeight = -Math.abs(rectHeight);
			else rectHeight = Math.abs(rectHeight);
		} else {
			rectHeight = y - mouseYStart;
			rectWidth = rectHeight * width / height;
			if (x < mouseXStart) rectWidth = -Math.abs(rectWidth);
			else rectWidth = Math.abs(rectWidth);
		}
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
//		mouseXCurrent = xMin + e.getX() * (xMax - xMin) / width;
//		mouseYCurrent = yMin + e.getY() * (yMax - yMin) / height;
		mouseXCurrent = e.getX();
		mouseYCurrent = e.getY();
		repaint();
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
			maxIterations += 100;
			System.out.println("Iterations = " + maxIterations);
			generateImage();
		} else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
			maxIterations -= 100;
			System.out.println("Iterations = " + maxIterations);
			generateImage();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			maxIterations = Integer.parseInt(JOptionPane.showInputDialog("Enter an integer"));
			System.out.println("Iterations = " + maxIterations);
			generateImage();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			double dist = Math.abs(yMin - yMax) * .1;
			yMin += dist;
			yMax += dist;
			generateImage();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			double dist = Math.abs(yMin - yMax) * .1;
			yMin -= dist;
			yMax -= dist;
			generateImage();
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			double dist = Math.abs(xMin - xMax) * .1;
			xMin += dist;
			xMax += dist;
			generateImage();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			double dist = Math.abs(xMin - xMax) * .1;
			xMin -= dist;
			xMax -= dist;
			generateImage();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE){
			try {
				File outputfile = new File(JOptionPane.showInputDialog("Enter a name") + ".png");
			    ImageIO.write(image, "png", outputfile);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_1) {
			color1 = JColorChooser.showDialog(this, "Choose first color", color1);
			generateImage();
		} else if (e.getKeyCode() == KeyEvent.VK_2) {
			color2 = JColorChooser.showDialog(this, "Choose second color", color2);
			generateImage();
		} else if (e.getKeyCode() == KeyEvent.VK_3) {
			color3 = JColorChooser.showDialog(this, "Choose the inner color", color3);
			generateImage();
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double amt = .0001 * e.getPreciseWheelRotation();
		System.out.println(amt);
		juliaA+=amt;
		generateImage();
	}
	
	private class ConstantsFrame extends JFrame {
		private static final long serialVersionUID = 1L;
		JPanel mainPanel;
		JButton[] up, up2;
		JButton[] down, down2;
		JTextField juliaABox, juliaBBox;
		public ConstantsFrame() {
			 mainPanel = new JPanel(new GridBagLayout());
			 int numButtons = 5;
			 up = new JButton[numButtons];
			 for (int i = 0; i < up.length; i++) {
				 up[i] = new JButton("/\\");
				 final int myNum = i;
				 up[i].addActionListener(new ActionListener() {
					 @Override
					public void actionPerformed(ActionEvent e) {
						 double amt = 1 * Math.pow(10, -(myNum));
						 juliaA += amt;
						 generateImage();
					}
				 });
			 }
			 
			 juliaABox = new JTextField("" + juliaA);
			 
			 down = new JButton[numButtons];
			 for (int i = 0; i < down.length; i++) {
				 down[i] = new JButton("\\/");
				 final int myNum = i;
				 down[i].addActionListener(new ActionListener() {
					 @Override
					public void actionPerformed(ActionEvent e) {
						 double amt = 1 * Math.pow(10, -(myNum));
						 juliaA -= amt;
						 generateImage();
					}
				 });
			 }
			 
			 up2 = new JButton[numButtons];
			 for (int i = 0; i < up2.length; i++) {
				 up2[i] = new JButton("/\\");
				 final int myNum = i;
				 up2[i].addActionListener(new ActionListener() {
					 @Override
					public void actionPerformed(ActionEvent e) {
						 double amt = 1 * Math.pow(10, -(myNum));
						 juliaB += amt;
						 generateImage();
					}
				 });
			 }
			 juliaBBox = new JTextField("" + juliaB);
			 down2 = new JButton[numButtons];
			 for (int i = 0; i < down2.length; i++) {
				 down2[i] = new JButton("\\/");
				 final int myNum = i;
				 down2[i].addActionListener(new ActionListener() {
					 @Override
					public void actionPerformed(ActionEvent e) {
						 double amt = 1 * Math.pow(10, -(myNum));
						 juliaB -= amt;
						 generateImage();
					}
				 });
			 }
			 GridBagConstraints gbc = new GridBagConstraints();
			 gbc.fill = GridBagConstraints.HORIZONTAL;
			 gbc.gridx = 0;
			 gbc.gridy = 0;
			 for (int i = 0; i < up.length; i++) {
				 mainPanel.add(up[i], gbc);
				 gbc.gridx++;
			 }
			 
			 gbc.gridy = 1;
			 gbc.gridx = 0;
			 gbc.ipady = 20;
			 mainPanel.add(new JLabel("Julia A:"), gbc);
			 gbc.gridwidth = numButtons-1;
			 gbc.gridx = 1;
			 mainPanel.add(juliaABox, gbc);
			 
			 gbc.gridwidth = 1;
			 gbc.ipady = 0;
			 gbc.gridy = 2;
			 gbc.gridx = 0;
			 for (int i = 0; i < down.length; i++) {
				 mainPanel.add(down[i], gbc);
				 gbc.gridx++;
			 }
			 
			 gbc.gridy = 3;
			 gbc.gridx = 0;
			 for (int i = 0; i < up2.length; i++) {
				 mainPanel.add(up2[i], gbc);
				 gbc.gridx++;
			 }
			 
			 gbc.gridy = 4;
			 gbc.gridx = 0;
			 gbc.ipady = 20;
			 mainPanel.add(new JLabel("Julia B:"), gbc);
			 gbc.gridx = 1;
			 gbc.gridwidth = numButtons-1;
			 mainPanel.add(juliaBBox, gbc);
			 
			 gbc.gridwidth = 1;
			 gbc.ipady = 0;
			 gbc.gridy = 5;
			 gbc.gridx = 0;
			 for (int i = 0; i < down2.length; i++) {
				 mainPanel.add(down2[i], gbc);
				 gbc.gridx++;
			 }
			 add(mainPanel);
			 pack();
		}
	}
}
