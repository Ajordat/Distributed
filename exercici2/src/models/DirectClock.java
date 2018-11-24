package models;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings("WeakerAccess")
public class DirectClock {
	private int[] clock;
	private int id;

	public DirectClock(int nThreads, int id) {
		this.id = id;
		this.clock = new int[nThreads];

		for (int i = 0; i < nThreads; i++)
			clock[i] = 0;
		clock[id] = 1;
	}

	public int getValue(int index) {
		return clock[index];
	}

	public void tick() {
		clock[id]++;
	}

	public void sendAction() {
		tick();
	}

	public void receiveAction(int sender, int sentValue) {
		clock[sender] = Math.max(clock[sender], sentValue);
		clock[id] = Math.max(clock[id], sentValue) + 1;
	}
}
