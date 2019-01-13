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
	private int startPosition;

	private static boolean found = false;
	private static int valueIndex = -1;

	SearchThread(int id, int value, int[] array, int startPosition, long startTime) {
		this.id = id;
		this.array = array;
		this.value = value;
		this.startPosition = startPosition;
		this.startTime = startTime;
	}

	@Override
	public void run() {

		int i = 0;
		for (int value : array) {
			if (this.value == value) {
				long endTime = System.nanoTime();
				synchronized (this) {
					if (!found) {
						found = true;
						valueIndex = startPosition + i;
						//System.out.print((endTime - this.startTime) / 1000);
						System.out.println("[Thread " + id + "] Found value " + value +
								" at index " + valueIndex +
								" in " + ((endTime - this.startTime) / 1000) + "us.");
					}
				}
				return;
			}
			i++;
		}

//		System.out.println("[Thread " + id + "] Couldn't find value " + this.value + ".");
	}

	static int getValueIndex() {
		return valueIndex;
	}

	static void resetSearch() {
		found = false;
		valueIndex = -1;
	}
}
