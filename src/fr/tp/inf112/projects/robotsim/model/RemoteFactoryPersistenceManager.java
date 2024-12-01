
package fr.tp.inf112.projects.robotsim.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import fr.tp.inf112.projects.canvas.view.FileCanvasChooser;

public class RemoteFactoryPersistenceManager {

	private String serverAddress;
	private int serverPort;

	public RemoteFactoryPersistenceManager(String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	public RemoteFactoryPersistenceManager(FileCanvasChooser canvasChooser) {
		// TODO Auto-generated constructor stub
	}

	public void persist(Factory factory) throws IOException {
		try (Socket socket = new Socket(serverAddress, serverPort);
				ObjectOutputStream objOutStr = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream objInStr = new ObjectInputStream(socket.getInputStream())) {

			objOutStr.writeObject(factory);
			objOutStr.flush();

			// Read response from server
			String response = (String) objInStr.readObject();
			System.out.println(response);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Factory read(String fileName) throws IOException, ClassNotFoundException {
		try (Socket socket = new Socket(serverAddress, serverPort);
				ObjectOutputStream objOutStr = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream objInStr = new ObjectInputStream(socket.getInputStream())) {

			objOutStr.writeObject(fileName);
			objOutStr.flush();

			// Read factory object from server
			return (Factory) objInStr.readObject();
		}
	}
}
