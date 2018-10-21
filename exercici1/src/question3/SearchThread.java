package question3;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class SearchThread implements Logger, Runnable {

	private int id;
	private Direction direction;
	private LinkedList<Integer> list;
	private Iterator<Integer> iterator;
	private int value;

	public enum Direction {
		LEFT, RIGHT
	}

	public SearchThread(int id, int value, Direction direction, LinkedList<Integer> list) {
		this.id = id;
		this.direction = direction;
		this.list = list;
		this.value = value;

		if (this.direction == Direction.RIGHT)
			this.iterator = this.list.iterator();
		else
			this.iterator = this.list.descendingIterator();
	}

	@Override
	public void run() {
		int value;

		while (this.iterator.hasNext()) {
			value = this.iterator.next();

			if (this.value == value) {
				this.log(this.id, Status.FOUND, value);
				return;
			}
		}
		this.log(this.id, Status.NOT_FOUND, this.value);
	}
}
