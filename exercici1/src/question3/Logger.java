package question3;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class Logger {

	private int id;

	Logger(int id) {
		this.id = id;
	}

	public void print(String string) {
		System.out.println("[Thread " + id + "] " + string);
	}

}
