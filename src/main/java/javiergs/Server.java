package javiergs;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private final PongPublisher pongPublisher;
	
	public Server() throws IOException {
		pongPublisher = new PongPublisher("pongServer", "pongClient","server");
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000 / 30);
				send();
				//receive();
			} catch (Exception e) {
				// throw new RuntimeException(e);
			}
		}
	}
	
	private void send() throws IOException {
		System.out.println("Sending");
		PongData pongData = PongBrain.getInstance().getPongData().clone();
		pongPublisher.sendData(pongData);
	}

	
	public boolean isReady() {
		return pongPublisher.isConnected();
	}
	
}