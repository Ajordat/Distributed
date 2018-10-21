package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Interfície que aporta els mètodes per a iniciar correctament els processos mitjançant fitxers.
 */
@SuppressWarnings("unused")
public interface StartFromFile {

	/**
	 * Atura l'execució fins que es detecta l'existència d'un fitxer.
	 * @param trigger Nom del fitxer.
	 */
	default void waitToStart(String trigger) {
		boolean flag;

		do {
			File f = new File(trigger);
			flag = f.exists();
		} while (flag);
	}

	/**
	 * Llegeix un fitxer, obté el valor escrit, escriu el següent i retorna el llegit.
	 *
	 * @param trigger Nom del fitxer.
	 * @return Torn llegit.
	 */
	default int getTurn(String trigger) {
		int turn = -1;

		try {
			File file = new File(trigger);
			Scanner scanner = new Scanner(file);
			turn = scanner.nextInt();
			scanner.close();

			if (!file.delete())
				throw new IOException("delete failure");

			PrintWriter pw = new PrintWriter(new FileWriter(trigger));
			pw.write(Integer.toString(turn + 1));
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return turn;
	}

	/**
	 * Possa el fitxer de torns a 0.
	 * @param trigger Nom del fitxer.
	 */
	default void resetTurn(String trigger) {

		try {
			File file = new File(trigger);

			if (!file.delete())
				throw new IOException("delete failure");

			PrintWriter pw = new PrintWriter(new FileWriter(trigger));
			pw.write("0");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
