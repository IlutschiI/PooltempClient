package at.pooltemp.client.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFinder {

	private static PropertyFinder instance;

	private Properties properties = new Properties();
	private InputStream propertyFile;

	private PropertyFinder() throws FileNotFoundException, IOException {
		propertyFile = new FileInputStream("application.properties");
		properties.load(propertyFile);
	}

	public static PropertyFinder getInstance() {
		if (instance == null) {
			try {
				instance = new PropertyFinder();
			} catch (FileNotFoundException e) {
				// TODO Logger
				System.out.println("File nicht gefunden!!!");
			} catch (IOException e) {
				// TODO Logger
				System.out.println("File konnte nicht geladen werde!!!");
			}
		}
		return instance;
	}

	public String findProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

}
