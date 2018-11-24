package models;

import network.Frame;
import network.LamportData;

import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class RAMutex extends BaseServer {
	private static final int N = Role.HWB.getLightWeightCount() - 1;
	private int myts;
	private LamportClock c = new LamportClock();
	private LinkedList pending = new LinkedList<Integer>();
	private int numOkay = 0;
	private int[] broadcastAddresses;

	public RAMutex(Role role) {
		this.myts = Integer.MAX_VALUE;
		this.broadcastAddresses = role.getBroadcastAddresses();
		this.role = role;
	}

	public void requestCS() throws IOException, ClassNotFoundException, InterruptedException {
		c.tick();
		myts = c.getValue();
		broadcast(broadcastAddresses, Frame.Type.REQUEST_CS, myts);
		numOkay = 0;
		while (numOkay < N)
			Thread.sleep(1000);
	}

	public void releaseCS() throws IOException, ClassNotFoundException {
		myts = Integer.MAX_VALUE;
		while (!pending.isEmpty())
			request((Integer) pending.pop(), Frame.Type.ACKNOWLEDGE, new LamportData(role, c.getValue()));
	}
	// tag, m, src
	public void handleMsg(Frame.Type type, int msg, Role src) throws IOException, ClassNotFoundException {

		c.receiveAction(src.getIndex(), msg);
		this.reply(Frame.Type.REPLY_OK);

		if (type == Frame.Type.REQUEST_CS) {
			if (myts == Integer.MAX_VALUE || msg < myts || (msg == myts && src.getIndex() < role.getIndex()))
				this.request(src.getPort(), Frame.Type.ACKNOWLEDGE, new LamportData(role, c.getValue()));
			else {
				pending.add(src.getPort());
			}

		} else if (type == Frame.Type.ACKNOWLEDGE) {
			numOkay++;
		}
	}
}
