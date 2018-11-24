package models;

import network.Frame;
import network.LamportData;

import java.io.IOException;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings("StatementWithEmptyBody")
public class CentMutex extends BaseServer {
	private static final int N = Role.HWA.getLightWeightCount();
	private DirectClock v;
	private int[] q;
	private int id;
	private int[] broadcastAddresses;

	public CentMutex(Role role) {
		this.q = new int[N];
		this.role = role;
		this.broadcastAddresses = role.getBroadcastAddresses();
		this.id = role.getIndex();
		this.v = new DirectClock(N, id);
		//this.setVerbose();

		for (int i = 0; i < N; i++)
			q[i] = Integer.MAX_VALUE;
	}

	public void requestCS() throws IOException, ClassNotFoundException, InterruptedException {
		v.tick();
		q[id] = v.getValue(id);
		broadcast(broadcastAddresses, Frame.Type.REQUEST_CS, q[id]);
		while (!okayCS())
			Thread.sleep(500);
	}

	public void releaseCS() throws IOException, ClassNotFoundException {
		q[id] = Integer.MAX_VALUE;
		broadcast(broadcastAddresses, Frame.Type.RELEASE_CS, v.getValue(id));
	}

	private boolean okayCS() {
		for (int i = 0; i < N; i++) {
			if (isGreater(q[i], i) || isGreater(v.getValue(i), i))
				return false;
		}
		return true;
	}

	private boolean isGreater(int entry2, int pid2) {
		if (entry2 == Integer.MAX_VALUE) return false;
		return (q[id] > entry2) || (q[id] == entry2 && id > pid2);
	}

	public void handleMsg(Frame.Type type, int msg, Role src) throws IOException {

		v.receiveAction(src.getIndex(), msg);

		if (type == Frame.Type.REQUEST_CS) {
			this.reply(Frame.Type.ACKNOWLEDGE, new LamportData(role, v.getValue(id)));
			q[src.getIndex()] = msg;
		} else if (type == Frame.Type.RELEASE_CS) {
			this.reply(Frame.Type.REPLY_OK);
			q[src.getIndex()] = Integer.MAX_VALUE;
		}
	}

}
