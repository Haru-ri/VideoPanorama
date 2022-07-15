package ua.program;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIMontage extends JFrame {

	private JButton savedButton = new JButton("Save as...");
	private JCheckBox leftToRightBox = new JCheckBox();
	private JLabel leftToRightBoxText = new JLabel("Left to right:");
	private JTextField sizeSlices = new JTextField("5",5);
	private JLabel sizeSlicesText = new JLabel("Size of slice:");

	private volatile ArrayList<byte[]> frames;
	private volatile ArrayList<Integer> mouseX;
	private volatile ArrayList<Integer> mouseY;

	public GUIMontage(ArrayList<byte[]> framesFromVideo, ArrayList<Integer> mouseXfromVideo, ArrayList<Integer> mouseYfromVideo) throws IOException {
		//Setting up the window and accepting params from the previous window
		super("Edition");
		this.mouseX=mouseXfromVideo;
		this.mouseY=mouseYfromVideo;
		this.frames=framesFromVideo;
		setVisible(true);
		setBounds(500, 0, 450, 80);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		JPanel panelTop = new JPanel(new FlowLayout());
		JPanel panelCenter = new JPanel(new FlowLayout());
		panelTop.add(leftToRightBoxText);
		panelTop.add(leftToRightBox);
		panelTop.add(sizeSlicesText);
		panelTop.add(sizeSlices);
		panelTop.add(savedButton);

		add(panelTop, BorderLayout.NORTH);
		add(panelCenter);
		
		savedButton.addActionListener(new RenderButtonListener());

	}

	class RenderButtonListener extends Thread implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			int size = Integer.parseInt(sizeSlices.getText());
			InputStream is = new ByteArrayInputStream(frames.get(0));
			BufferedImage bi = null;
			try {
				bi = ImageIO.read(is);
			} catch (IOException exception) {
				exception.printStackTrace();
			}

			BufferedImage rowImage = new BufferedImage((frames.size()*size),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
			Graphics2D g2dRow = rowImage.createGraphics();

			if(!leftToRightBox.isSelected()) {
				for(int i = 0; i<frames.size(); i++) { 
					is = new ByteArrayInputStream(frames.get(i));
					try {
						bi = ImageIO.read(is);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					BufferedImage frame = bi.getSubimage(size-(size/2), 0, size+(size/2), bi.getHeight());
					g2dRow.drawImage(frame,size*i,0, null);
				}
			} else {
				for(int i = frames.size(); 0<i; i--) { 
					is = new ByteArrayInputStream(frames.get(i-1));
					try {
						bi = ImageIO.read(is);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					BufferedImage frame = bi.getSubimage(size-(size/2), 0, size+(size/2), bi.getHeight());
					g2dRow.drawImage(frame,size*(frames.size()-i),0, null);
				}
			}

			try {
				ImageIO.write(rowImage, "png", new File("panorama.png"));
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

}
