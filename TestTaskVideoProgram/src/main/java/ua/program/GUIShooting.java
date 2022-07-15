package ua.program;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIShooting extends JFrame {
	private JButton uploadButton = new JButton("Upload...");
	private JButton goSetPositionButton = new JButton("Set position for images");
	private JLabel textSpeed = new JLabel("Playback speed:");
	private JTextField speedField = new JTextField("1",5);
	private File video;

	public GUIShooting() {
		//Setting up window
		super("Select video");
		setBounds(500,0,340,125);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);

		uploadButton.addActionListener(new UploadBottomListener());
		goSetPositionButton.addActionListener(new GoSetPositionListener(this));

		setLayout(new BorderLayout());
		JPanel panelTop = new JPanel(new FlowLayout());
		JPanel panelCenter = new JPanel(new FlowLayout());
		panelTop.add(uploadButton);
		panelTop.add(textSpeed);
		panelTop.add(speedField);
		panelCenter.add(goSetPositionButton);
		goSetPositionButton.setVisible(false);
		add(panelTop, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);

	}
	
	class GoSetPositionListener implements ActionListener {
		JFrame jFrame;
		public GoSetPositionListener(JFrame jframe) {
			this.jFrame=jframe;
		}

		public void actionPerformed(ActionEvent e) {
			//Go to shooting phase
			jFrame.setVisible(false);
			new GUISetPosition(video, Integer.parseInt(speedField.getText()));
		}
	}

	class UploadBottomListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Selected file(video)
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.showOpenDialog(fileChooser);

			video = fileChooser.getSelectedFile();
			//When we have selected a file, the jump button becomes visible
			if(video.exists()) {
				goSetPositionButton.setVisible(true);
			}
		}
	}
}



