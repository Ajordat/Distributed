package question7;

import utils.ArrayFactory;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings({"WeakerAccess", "unused"})
public class ParallelSearch {

	private static final int ARRAY_SIZE = 10000;

	public static void parallelMergeSort(int[] numbers) {

		float offset = (float) numbers.length / 2;
		Thread thread;
		long startTime = 0, endTime = 0;

		// i*o -> (i+1)*o-1
		SearchThread.setArray(numbers);

		thread = new Thread(new SearchThread(1, 0, numbers.length - 1));

		try {
			startTime = System.nanoTime();
			thread.start();
			thread.join();
			endTime = System.nanoTime();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.print((endTime - startTime) / 1000);
		//System.out.println("[Parallel] Sorted array in " + ((endTime - startTime) / 1000) + "us.");
	}

	public static void sequentialMergeSort(int[] numbers) {

		SearchThread thread = new SearchThread(numbers);
		long startTime, endTime;


		startTime = System.nanoTime();
		thread.mergeSort(0, numbers.length - 1);
		endTime = System.nanoTime();

		System.out.print((endTime - startTime) / 1000);
	}

	public static void main(String[] args) {

		for (int j = 10; j < 5001; j += 10) {
			System.out.print(j + "-");
			for (int i = 0; i < 10; i++) {
				int[] numbers = ArrayFactory.create(j, ArrayFactory.Method.DESCENDING, 0);
				ParallelSearch.sequentialMergeSort(numbers);
				if (i < 9) System.out.print(',');
			}
			System.out.println();
		}
	}
}
