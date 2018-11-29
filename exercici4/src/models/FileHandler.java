package models;

import java.io.*;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class FileHandler {
	private String filename;
	private final static String RELATIVE_PATH = "logs\\";

	public FileHandler(String filename) {
		this.filename = RELATIVE_PATH + filename;
	}

	public int getValue(int variable) {
		String line;
		int current;
		File input = new File(filename);

		if (!input.exists())
			return -1;

		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {

			while ((line = reader.readLine()) != null) {
				current = Integer.parseInt(line.substring(0, line.indexOf(':')));
				if (variable > current) {
					reader.close();
					return -1;
				} else if (variable == current) {
					reader.close();
					return Integer.parseInt(line.substring(line.lastIndexOf(',') + 1));
				}
			}

		} catch (IOException e) {}

		return -1;
	}

	public void setValue(int variable, int value) {
		String line;
		int current;
		boolean found = false;
		File inputFile = new File(filename);
		File tempFile = new File(filename + ".tmp");

		try {
			inputFile.getParentFile().mkdirs();
			inputFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

			while ((line = reader.readLine()) != null) {
				current = Integer.parseInt(line.substring(0, line.indexOf(':')));

				if (variable < current && !found) {
					writer.write(variable + ":," + value + "\n");
					writer.write(line + "\n");
					found = true;
				} else if (variable == current) {
					writer.write(line + "," + value + "\n");
					found = true;
				} else {
					writer.write(line + "\n");
				}
			}

			if (!found)
				writer.write(variable + ":," + value + "\n");

		} catch (IOException ignored) {}
		finally {
			inputFile.delete();
			tempFile.renameTo(inputFile);
		}

	}
}
