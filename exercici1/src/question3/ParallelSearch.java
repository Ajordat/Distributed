package question3;

import java.util.LinkedList;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings("WeakerAccess")
public class ParallelSearch{

	private LinkedList<Integer> list;

	public ParallelSearch(int[] numbers) {
		this.list = new LinkedList<>();
		for (int i : numbers)
			this.list.add(i);
	}

	public void startSearch(int value) {
		SearchThread sRight = new SearchThread(1, value, SearchThread.Direction.RIGHT, this.list);
		SearchThread sLeft = new SearchThread(2, value, SearchThread.Direction.LEFT, this.list);

		new Thread(sRight).start();
		new Thread(sLeft).start();
	}

	public static void main(String[] args) {
		int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

		ParallelSearch ps = new ParallelSearch(numbers);

		ps.startSearch(3);
	}

}
