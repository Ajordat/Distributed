package models;

import java.io.*;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class FileHandler {
	private String filename;

	public FileHandler(String filename) {
		this.filename = filename;
	}

	public int getValue(int variable) {
		String line;
		int current;

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

			while ((line = reader.readLine()) != null) {
				current = Integer.parseInt(line.substring(0, line.indexOf(':')));
				if (variable == current) {
					reader.close();

					return Integer.parseInt(line.substring(line.lastIndexOf(',') + 1));
				}
			}

		} catch (IOException ignored) {}

		return -1;
	}

	public void setValue(int variable, int value) {
		String line;
		int current;
		boolean found = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filename));
			 BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".tmp"))) {

			while ((line = reader.readLine()) != null) {
				current = Integer.parseInt(line.substring(0, line.indexOf(':')));
				if (variable == current) {
					writer.write(line + "," + value);
					found = true;
				} else {
					writer.write(line);
				}
			}

			if (!found) {
				writer.write(variable + ":," + value);
			}

		} catch (IOException ignored) {}

	}
}
