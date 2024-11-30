package fr.tp.inf112.projects.robotsim.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import fr.tp.inf112.projects.canvas.view.FileCanvasChooser;

public class RemoteFileCanvasChooser extends FileCanvasChooser {

	// constructor
	public RemoteFileCanvasChooser() {
		super(null, null);
	}

	public RemoteFileCanvasChooser(String string, String string2) {
		super(string, string2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String browseCanvases(boolean open) {
		if (open) {
			// Request list of model files from the persistence server
			String[] modelFiles = getModelFilesFromServer();

			// Display the list of files in a combo box
			String selectedFile = (String) JOptionPane.showInputDialog(null, "Select a model file to open:",
					"Open Model File", JOptionPane.PLAIN_MESSAGE, null, modelFiles, modelFiles[0]);

			return selectedFile;
		} else {
			// Prompt the user to enter a file name
			String fileName = JOptionPane.showInputDialog(null, "Enter the name of the model file:", "Save Model File",
					JOptionPane.PLAIN_MESSAGE);

			return fileName;
		}
	}

	private String[] getModelFilesFromServer() {
		List<String> modelFiles = new ArrayList<>();
		try {
			URL url = new URL("http://persistence-server-url/models");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				modelFiles.add(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelFiles.toArray(new String[0]);
	}
}
