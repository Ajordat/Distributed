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

				if (variable < current)
					return -1;
				else if (variable == current)
					return Integer.parseInt(line.substring(line.lastIndexOf(',') + 1));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

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
					if (value != Integer.parseInt(line.substring(line.lastIndexOf(',') + 1)))
						writer.write(line + "," + value + "\n");
					else
						writer.write(line + "\n");
					found = true;
				} else {
					writer.write(line + "\n");
				}
			}

			if (!found)
				writer.write(variable + ":," + value + "\n");

		} catch (IOException ignored) {
		} finally {
			inputFile.delete();
			tempFile.renameTo(inputFile);
		}
	}

	public String toTransaction() {
		String line;
		String variable, value;
		File input = new File(filename);
		StringBuilder transaction = new StringBuilder();

		if (!input.exists())
			return "";

		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {

			while ((line = reader.readLine()) != null) {
				variable = line.substring(0, line.indexOf(':'));
				value = line.substring(line.lastIndexOf(',') + 1);

				if (!transaction.toString().isEmpty())
					transaction.append(',');
				transaction.append("w(").append(variable).append(",").append(value).append(")");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return transaction.toString();
	}
}
