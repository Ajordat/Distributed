package question4;

import utils.ArrayFactory;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings({"WeakerAccess", "unused"})
public class ParallelSearch {
	//  500 -> 378
	// 1000 -> 755
	// 1500 -> 1133
	// 2000 -> 1510
	// 2500 -> 1888
	// 3000 -> 2265
	// 3500 -> 2643
	private static final int ARRAY_SIZE = 10000;
	private static final int VALUE = 7550;
	private static final int THREADS = 500;
	private static final int SAMPLE_ITERATIONS = 20;

	public static void startSearch(int value, int[] numbers, int n_threads) {

		float offset = (float) numbers.length / n_threads;
		Thread[] threads = new Thread[n_threads];

		// i*o -> (i+1)*o-1

		long startTime = System.nanoTime();
		for (int i = 0; i < n_threads; i++) {
			int size = Math.round((i + 1) * offset) - Math.round(i * offset);
			int[] array = new int[size];
			System.arraycopy(numbers, Math.round(i * offset), array, 0, size);
			SearchThread thread = new SearchThread(i + 1, value, array, startTime);
			threads[i] = new Thread(thread);
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
		for (int current : numbers) {
			if (current == value) {
				long endTime = System.nanoTime();
				System.out.print((endTime - startTime) / 1000);
				return;
			}
		}
		/*
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == value) {
				long endTime = System.nanoTime();
				//System.out.println("Found value " + value + " on index " + i + " in " + ((endTime - startTime) / 1000) + "us.");
				System.out.print((endTime - startTime) / 1000);
				return;
			}
		}*/

	}

	public static void main(String[] args) {
		int[] numbers = ArrayFactory.create(ARRAY_SIZE, ArrayFactory.Method.ASCENDING, 0);

		//System.out.println("Threads: 1");
		for (int n = 0; n < SAMPLE_ITERATIONS; n++) {
			ParallelSearch.sequentialSearch(VALUE, numbers);
			if (n != SAMPLE_ITERATIONS - 1)
				System.out.print(", ");
		}
		System.out.println();

		for (int threads = 2; threads <= THREADS; threads++) {
			//System.out.println("Threads: " + threads);
			for (int n = 0; n < SAMPLE_ITERATIONS; n++) {
				ParallelSearch.startSearch(VALUE, numbers, threads);
				if (n != SAMPLE_ITERATIONS - 1)
					System.out.print(", ");
			}
			System.out.println();
		}
		//ParallelSearch.startSearch(VALUE, numbers, THREADS);
		//ParallelSearch.sequentialSearch(VALUE, numbers);
	}

}
