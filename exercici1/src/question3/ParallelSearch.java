package question3;

import utils.ArrayFactory;

import java.util.Arrays;
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
		int[] numbers = ArrayFactory.create(100, ArrayFactory.Method.ASCENDING, 0);

		ParallelSearch ps = new ParallelSearch(numbers);

		ps.startSearch(80);
	}

}
