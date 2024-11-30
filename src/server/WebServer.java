package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	public static void main(String args[]) {
		try (ServerSocket serverSocket = new ServerSocket(80);) {
			do {
				try {
					Socket socket = serverSocket.accept();
					Runnable reqProcessor = new RequestProcessor(socket);
					new Thread(reqProcessor).start();
				} catch (IOException ex) {
					// - handle exceptions

				}
			} while (true);
		} catch (IOException ex) {
			// - handle exceptions

		}
	}
}