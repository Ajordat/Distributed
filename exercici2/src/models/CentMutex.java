package models;

import network.Frame;
import network.LamportData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings("StatementWithEmptyBody")
public class CentMutex extends BaseServer {
	private static final int N = 3;
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
		this.setVerbose();

		for (int i = 0; i < N; i++)
			q[i] = Integer.MAX_VALUE;
	}

	public void requestCS() throws IOException, ClassNotFoundException {
		v.tick();
		q[id] = v.getValue(id);
		broadcast(Frame.Type.REQUEST_CS, q[id]);
		while (!okayCS()) ;
	}

	public void releaseCS() throws IOException, ClassNotFoundException {
		q[id] = Integer.MAX_VALUE;
		stderr("Releasing...");
		broadcast(Frame.Type.RELEASE_CS, v.getValue(id));
		stderr("Released");
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
			q[src.getIndex()] = msg;
			this.reply(Frame.Type.ACKNOWLEDGE, new LamportData(role, v.getValue(id)));
		} else if (type == Frame.Type.RELEASE_CS) {
			this.reply(Frame.Type.REPLY_OK);
			q[src.getIndex()] = Integer.MAX_VALUE;
		}
	}

	private void broadcast(Frame.Type type, int value) throws IOException, ClassNotFoundException {
		for (int address : broadcastAddresses) {
			verbose("Requesting to " + address);
			request(address, type, new LamportData(role, value));
		}
	}

	public void setStreams(ObjectInputStream input, ObjectOutputStream output) {
		this.inputStream = input;
		this.outputStream = output;
	}
}
