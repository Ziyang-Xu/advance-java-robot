
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import fr.tp.inf112.projects.canvas.model.Canvas;
import fr.tp.inf112.projects.robotsim.model.Factory;
import fr.tp.inf112.projects.robotsim.model.FactoryPersistenceManager;

public class RequestProcessor implements Runnable {
	private Socket socket;

	public RequestProcessor(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			InputStream inpStr = socket.getInputStream();
			ObjectInputStream objInStr = new ObjectInputStream(inpStr);

			// Read and decode input request
			Object receivedObject = objInStr.readObject();
			OutputStream outStr = socket.getOutputStream();
			ObjectOutputStream objOutStr = new ObjectOutputStream(outStr);

			FactoryPersistenceManager persistenceManager = new FactoryPersistenceManager(null);

			if (receivedObject instanceof String) {
				// Read factory model
				String fileName = (String) receivedObject;
				Canvas factory = persistenceManager.read(fileName);
				objOutStr.writeObject(factory);
			} else if (receivedObject instanceof Factory) {
				// Persist factory model
				Factory factory = (Factory) receivedObject;
				persistenceManager.persist(factory);
				objOutStr.writeObject("Factory model persisted successfully.");
			} else {
				objOutStr.writeObject("Unknown object type received.");
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
