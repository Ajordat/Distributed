package question7;

import utils.ArrayFactory;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings({"WeakerAccess", "unused"})
public class SortManager {

	private static final int ARRAY_SIZE = 10000;

	public static void parallelMergeSort(int[] numbers) {

		float offset = (float) numbers.length / 2;
		Thread thread;
		long startTime = 0, endTime = 0;

		// i*o -> (i+1)*o-1
		MergeSortThread.setArray(numbers);

		thread = new Thread(new MergeSortThread(1, 0, numbers.length - 1));

		try {
			startTime = System.nanoTime();
			thread.start();
			thread.join();
			endTime = System.nanoTime();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//System.out.print((endTime - startTime) / 1000);
		System.out.println("[Parallel] Sorted array in " + ((endTime - startTime) / 1000) + "us.");
	}

	public static void sequentialMergeSort(int[] numbers) {

		MergeSortThread thread = new MergeSortThread(numbers);
		long startTime, endTime;

		startTime = System.nanoTime();
		thread.mergeSort(0, numbers.length - 1);
		endTime = System.nanoTime();

		//System.out.print((endTime - startTime) / 1000);
		System.out.println("[Sequential] Sorted array in " + ((endTime - startTime) / 1000) + "us.");
	}

	public static void main(String[] args) {

		int[] numbers = ArrayFactory.create(5000, ArrayFactory.Method.DESCENDING, 0);

		SortManager.parallelMergeSort(numbers);
		//SortManager.sequentialMergeSort(numbers);

		for (int i = 0; i < numbers.length; i++)
			if (numbers[i] != i)
				System.out.println("Liada parda");

	}
}
