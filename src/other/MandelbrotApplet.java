package other;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MandelbrotApplet extends Applet implements KeyListener {
	private static final long serialVersionUID = 1L;

	MandelbrotPanel panel;
	JTextField iterationsText;
	JButton update;
	public void init() {
		int sizeW = Integer.parseInt(getParameter("AppWidth"));
		int sizeH = Integer.parseInt(getParameter("AppHeight"));
		JPanel mainPanel = new JPanel(new BorderLayout());
		panel = new MandelbrotPanel(sizeW, sizeH, true);
		panel.generateImage();
		System.out.println("Width and Height " + sizeW + " " + sizeH);
		JLabel iterations = new JLabel("Iterations:");
		iterationsText = new JTextField();
		update = new JButton("Update");
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int iters = Integer.parseInt(iterationsText.getText());
					panel.maxIterations = iters;
					panel.generateImage();
				} catch (Exception exc) {
					System.out.println(exc.getMessage());
				}
			}
		});
		JPanel bottom = new JPanel(new BorderLayout());
		bottom.add(iterations, BorderLayout.WEST);
		bottom.add(iterationsText, BorderLayout.CENTER);
		bottom.add(update, BorderLayout.EAST);
		setLayout(new BorderLayout());
		addKeyListener(this);
		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.add(bottom, BorderLayout.SOUTH);
		add(mainPanel, BorderLayout.CENTER);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		panel.keyPressed(e);
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
