package question3;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class SearchThread implements Runnable {

	private int id;
	private Iterator<Integer> iterator;
	private int value;
	private static boolean found = false;
	private Logger logger;

	public enum Direction {
		LEFT, RIGHT
	}

	SearchThread(int id, int value, Direction direction, LinkedList<Integer> list) {
		this.id = id;
		this.value = value;
		this.logger = new Logger(id);

		if (direction == Direction.RIGHT)
			this.iterator = list.iterator();
		else
			this.iterator = list.descendingIterator();

	}

	@Override
	public void run() {

		while (this.iterator.hasNext()) {

			if (this.value == this.iterator.next()) {
				synchronized (this) {
					if (!found) {
						found = true;
						logger.print("Found value " + this.value + " first.");
					} else {
						logger.print("Found value " + this.value);
					}
				}
				return;
			}
		}
		logger.print("Couldn't find value " + this.value);
	}
}
