package models;

/**
 * @author Ajordat
 * @version 1.0
 **/
class Logger {
	private boolean verbose;
	private String header;

	Logger(String header, boolean verbose) {
		this.verbose = verbose;
		this.header = "[" + header + "] ";
	}

	private void print(String string) {
		System.out.println(header + string);
	}

	final void verbose(String string) {
		if (verbose)
			print(string);
	}

	final void error(String string) {
		System.err.println(header + string);
	}
}
