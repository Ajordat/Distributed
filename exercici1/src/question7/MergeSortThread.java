package question7;


/**
 * @author Ajordat
 * @version 1.0
 **/
public class MergeSortThread implements Runnable {

	private int id;
	private static int[] array;
	private int begin;
	private int end;

	MergeSortThread(int id, int begin, int end) {
		this.id = id;
		this.begin = begin;
		this.end = end;
	}

	MergeSortThread(int[] array) {
		MergeSortThread.array = array;
	}

	static void setArray(int[] array) {
		MergeSortThread.array = array;
	}

	void mergeSort(int begin, int end) {

		int size = end - begin + 1;
		int middle = begin + Math.round((float) size / 2);

		if (size > 2) {

			this.mergeSort(begin, middle - 1);
			this.mergeSort(middle, end);

			int[] copy = new int[size];
			System.arraycopy(array, begin, copy, 0, size);
			middle = Math.round((float) size / 2);

			int i = 0, j = middle, index = begin;

			for (; i < middle && j < size; index++)
				array[index] = copy[i] < copy[j] ? copy[i++] : copy[j++];

			for (; i < middle; index++, i++)
				array[index] = copy[i];

			for (; j < size; index++, j++)
				array[index] = copy[j];
		}

		if (array[begin] > array[end]) {
			int aux = array[begin];
			array[begin] = array[end];
			array[end] = aux;
		}
	}

	@Override
	public void run() {

		//System.out.println("[Thread " + id + "] Indexes [" + this.begin + ".." + this.end + "]");
		int size = this.end - this.begin + 1;
		int middle = this.begin + Math.round((float) size / 2);

		if (size > 2) {

			Thread t1 = new Thread(new MergeSortThread(this.id * 10 + 1, this.begin, middle - 1));
			Thread t2 = new Thread(new MergeSortThread(this.id * 10 + 2, middle, this.end));

			t1.start();
			t2.start();

			try {
				t1.join();
				t2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int[] copy = new int[size];
			System.arraycopy(array, this.begin, copy, 0, size);
			middle = Math.round((float) size / 2);

			int i = 0, j = middle, index = this.begin;

			for (; i < middle && j < size; index++)
				array[index] = copy[i] < copy[j] ? copy[i++] : copy[j++];

			for (; i < middle; index++, i++)
				array[index] = copy[i];

			for (; j < size; index++, j++)
				array[index] = copy[j];
		}

		if (array[this.begin] > array[this.end]) {
			int aux = array[this.begin];
			array[this.begin] = array[this.end];
			array[this.end] = aux;
		}
	}
}
