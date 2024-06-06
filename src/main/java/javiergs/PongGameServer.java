package javiergs;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Main class for the Pong Game Server.
 * It creates the GUI and starts the server.
 *
 * @author javiergs
 * @version 1.0
 */
public class PongGameServer extends JFrame {
	
	public PongGameServer() {
		setLayout(new GridLayout(1, 1));
		PongPanel gamePanel = new PongPanel();
		add(gamePanel);
	}
	
	public static void main (String[] args) {
		PongGameServer pongGame = new PongGameServer();
		// look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start server
		message("Server starting ....", JOptionPane.INFORMATION_MESSAGE);
		try {
			pongGame.connect();
		} catch (IOException e) {
			message("Server cannot run.", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		message("Server connected.", JOptionPane.INFORMATION_MESSAGE);
		// Create the GUI
		pongGame.setTitle("Pong Game Server | An Example of TCP Sockets in Java");
		pongGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pongGame.setSize(800, 600);
		pongGame.setResizable(false);
		pongGame.setLocation(0, 0);
		pongGame.setVisible(true);
	}
	
	private void connect() throws IOException {
		PongBrain.getInstance().setWhoAmI(PongBrain.SERVER);
		Server server = new Server();
		if (server.isReady()) {
			Thread thread = new Thread(server);
			thread.start();
		}
	}
	
	private static void message (String text, int icon) {
		JOptionPane.showMessageDialog(null, text, "Server", icon);
	}
	
}