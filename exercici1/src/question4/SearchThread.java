package question4;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class SearchThread implements Runnable {

	private int id;
	private int[] array;
	private int value;
	private long startTime;

	SearchThread(int id, int value, int[] array, long startTime) {
		this.id = id;
		this.array = array;
		this.value = value;
		this.startTime = startTime;
	}

	@Override
	public void run() {

		for (int value : array) {
			if (this.value == value) {
				long endTime = System.nanoTime();
				System.out.print((endTime - this.startTime) / 1000);
				return;
			}
		}
		/*
		int value;
		for (int i = 0; i < array.length; i++) {
			value = array[i];
			if (this.value == value) {
				long endTime = System.nanoTime();
				System.out.println("[Thread " + id + "] Found value " + value + " on index " + i + " in " + ((endTime - this.startTime) / 1000) + "us.");
				return;
			}
		}
		*/
		// System.out.println("[Thread " + id + "] Couldn't find value " + this.value + ".");
	}
}
