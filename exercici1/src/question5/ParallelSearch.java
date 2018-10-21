package question5;

import utils.ArrayFactory;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings({"WeakerAccess", "unused"})
public class ParallelSearch {

	private static final int ARRAY_SIZE = 1000;
	private static final int VALUE = 755;
	private static final int THREADS = 20;

	public static void startSearch(int value, int[] numbers, int n_threads) {

		float offset = (float) numbers.length / n_threads;
		Thread[] threads = new Thread[n_threads];

		// i*o -> (i+1)*o-1
		SearchThread.setArray(numbers);

		long startTime = System.nanoTime();
		for (int i = 0; i < n_threads; i++) {
			threads[i] = new Thread(new SearchThread(i + 1, value, Math.round(i * offset), Math.round((i + 1) * offset) - 1, startTime));
			threads[i].start();
		}

		try {
			for (Thread t : threads)
				t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sequentialSearch(int value, int[] numbers) {

		long startTime = System.nanoTime();
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == value) {
				long endTime = System.nanoTime();
				System.out.println("[Sequential] Found value " + value + " on index " + i + " in " + ((endTime - startTime) / 1000) + "us.");
				return;
			}
		}
		System.out.println("[Sequential] Couldn't find value " + value + ".");

	}

	public static void main(String[] args) {
		int[] numbers = ArrayFactory.create(ARRAY_SIZE, ArrayFactory.Method.ASCENDING, 0);

		ParallelSearch.startSearch(VALUE, numbers, THREADS);
		ParallelSearch.sequentialSearch(VALUE, numbers);
	}

}
