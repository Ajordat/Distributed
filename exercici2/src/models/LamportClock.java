package models;


/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings("WeakerAccess")
public class LamportClock {
	int c;

	public LamportClock() {
		c = 1;
	}

	public int getValue() { return c; }

	public void tick() { c++; }

	public int sendAction() { return ++c; }

	public void receiveAction(int src, int sentValue) {
		c = Math.max(c, sentValue) + 1;
	}
}
