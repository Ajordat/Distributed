package question5;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class SearchThread implements Runnable {

	private int id;
	private static int[] array;
	private int value;
	private long startTime;
	private int begin;
	private int end;

	SearchThread(int id, int value, int begin, int end, long startTime) {
		this.id = id;
		this.value = value;
		this.begin = begin;
		this.end = end;
		this.startTime = startTime;
	}

	static void setArray(int[] array) {
		SearchThread.array = array;
	}

	@Override
	public void run() {
		int value;

		//System.out.println("[Thread " + id + "] Indexes [" + this.begin + ".." + this.end + "]");

		for (int i = this.begin; i <= this.end; i++) {
			value = array[i];
			if (this.value == value) {
				long endTime = System.nanoTime();
//				System.out.print((endTime - this.startTime) / 1000);
				System.out.println("[Thread " + id + "] Found value " + value + " on index " + i + " in " + ((endTime - this.startTime) / 1000) + "us.");
				return;
			}
		}

//		System.out.println("[Thread " + id + "] Couldn't find value " + this.value + ".");
	}
}
