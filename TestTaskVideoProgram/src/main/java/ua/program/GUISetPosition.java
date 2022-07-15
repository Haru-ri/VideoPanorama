package ua.program;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class GUISetPosition extends JFrame implements MouseListener {

	private EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();
	private boolean SettingMode = false;
	private File film;
	private int speed;
	private ArrayList<byte[]> frames = new ArrayList<byte[]>();
	private ArrayList<Integer> mouseX = new ArrayList<Integer>();
	private ArrayList<Integer> mouseY = new ArrayList<Integer>();

	public GUISetPosition(File film, int speed) {
		//Setting up the window and accepting params from the previous window
		super(film.getName()+" - shooting");
		this.speed=speed;
		player.videoSurfaceComponent().addMouseListener(this);
		this.film = film;
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500,0,800,500);
		
		//Set video player on full window
		setContentPane(player);
		player.mediaPlayer().media().startPaused(film.toString());

	}

	public void mousePressed(MouseEvent e) {
		SettingMode = true;
		new PlayedMovie().start();
		CheckMouseClick checkMouseClick = new CheckMouseClick(e);
		checkMouseClick.start();
		
	}

	public void mouseReleased(MouseEvent e) {
		SettingMode=false;
		//stop the video
		player.release();
		setVisible(false);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		try {
			new GUIMontage(frames,mouseX,mouseY);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}



	class CheckMouseClick extends Thread {
		MouseEvent e;

		public CheckMouseClick(MouseEvent e){
			this.e=e;
		}

		@Override
		public void run() {
			//Video length in milliseconds
			long length = player.mediaPlayer().status().length();

			if(SettingMode) {
				for(int i = 0; i<(length/1000)/speed; i++) {
					if(SettingMode==false) { break; }
					mouseX.add(e.getX());
					mouseY.add(e.getY());
					new CreateImage().start();
					System.out.println(e.getX()+" "+e.getY());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

			}
		}
	}




	class PlayedMovie extends Thread {
		@Override
		public void run() {
			//start to play video
			player.mediaPlayer().media().play(film.toString());
			//set playback speed
			player.mediaPlayer().controls().setRate(speed);
		}
	}




	class CreateImage extends Thread {
		@Override
		public void run() {
			try {
				//Frame serialization
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(player.mediaPlayer().snapshots().get(), "jpg", outputStream);
				frames.add(outputStream.toByteArray());
			} catch (Exception e) {}
		}
	}
}

