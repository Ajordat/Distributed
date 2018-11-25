package models;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class Logger {
	private boolean debug;
	private String header;

	public Logger(boolean debug, String header) {
		this.debug = debug;
		this.header = "[" + header + "] ";
	}

	public Logger(boolean debug) {
		this.debug = debug;
		this.header = "";
	}

	public void print(String string) {
		System.out.println(header + string);
	}

	public final void debug(String string) {
		if (debug)
			print(string);
	}

	public final void error(String string) {
		System.err.println(header + string);
	}
}
