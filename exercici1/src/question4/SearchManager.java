package question4;

import utils.ArrayFactory;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class SearchManager {
	private static final int ARRAY_SIZE = 10000;
	private static final int VALUE = 7550;
	private static final int THREADS = 500;
	private static final int MIN_SIZE = 10;
	private static final int SAMPLE_ITERATIONS = 10;

	public static int parallelSearch(int value, int[] numbers, int n_threads) {

		float offset = (float) numbers.length / n_threads;
		Thread[] threads = new Thread[n_threads];

		// i*o -> (i+1)*o-1

		long startTime = System.nanoTime();

		for (int i = 0; i < n_threads; i++) {
			int startPosition = Math.round(i * offset);
			int size = Math.round((i + 1) * offset) - startPosition;
			int[] array = new int[size];

			System.arraycopy(numbers, startPosition, array, 0, size);
			threads[i] = new Thread(new SearchThread(i + 1, value, array, startPosition, startTime));
			threads[i].start();
		}

		try {
			for (Thread t : threads)
				t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		SearchThread.resetSearch();
		return SearchThread.getValueIndex();
	}

	public static void sequentialSearch(int value, int[] numbers) {
		long startTime = System.nanoTime();

		for (int current : numbers) {
			if (current == value) {
				long endTime = System.nanoTime();
				System.out.print((endTime - startTime) / 1000);
				return;
			}
		}
	}

	public static void main(String[] args) {
		int[] numbers = ArrayFactory.create(ARRAY_SIZE, ArrayFactory.Method.ASCENDING, 0);

		SearchManager.parallelSearch(VALUE, numbers, THREADS);
		//SearchManager.sequentialSearch(VALUE, numbers);
/*
		for (int t = 10; t < ARRAY_SIZE / MIN_SIZE; t += 50) {
			for (int n = 0; n < SAMPLE_ITERATIONS; n++) {
				SearchManager.parallelSearch(VALUE, numbers, t);
				if (n != SAMPLE_ITERATIONS - 1)
					System.out.print(", ");
			}
			System.out.println();
		}*/

	}

}
