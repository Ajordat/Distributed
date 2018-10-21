package question3;

/**
 * @author Ajordat
 * @version 1.0
 **/
public interface Logger {

	enum Status {
		FOUND,
		NOT_FOUND
	}


	default void log(int id, Status status, Object o) {
		String header = "[Thread " + id + "] " ;

		switch (status) {
			case FOUND:
				System.out.println(header + "Found value " + o);
				break;
			case NOT_FOUND:
				System.out.println(header + "Couldn't find value " + o);
				break;
		}
	}

}
