package staticServer;

import models.BaseServer;
import network.Frame;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class StaticServer extends BaseServer {

	private static final int MAX_SERVERS = 5;
	private boolean token;

	StaticServer() {
		super();

		int turn = this.getTurn("turn.txt");

		if (turn < 0) {
			verbose("There's no turn available", Role.SERVER);
			System.exit(-1);

		} else if (turn >= MAX_SERVERS) {
			verbose("Exceeded maximum servers", Role.SERVER);
			System.exit(-1);
		}
		this.port = INITIAL_PORT + turn;
		this.token = (turn == 0);

		if (turn == MAX_SERVERS - 1)
			this.nextAddress = INITIAL_PORT;

		else
			this.nextAddress = this.port + 1;
	}

	@Override
	protected void action(Frame frame) throws IOException {
		switch (frame.getType()) {
			case SEND_VALUE:
				Frame frameOut;
				int value = (Integer) frame.getData();

				if (value >= 0) {
					frameOut = new Frame(Frame.Type.REPLY_OK, value);
					this.updateCurrentValue(value);
					this.token = true;
				} else
					frameOut = new Frame(Frame.Type.REPLY_KO, value);

				this.outputStream.writeObject(frameOut);

				break;

			default:
				break;
		}
	}

	@Override
	public void run() {

		if (!this.open())
			return;

		this.isOn = true;

		try {
			while (this.isOn) {
				Socket client = this.server.accept();
				verbose("New connection received on port " + this.port, Role.SERVER);

				this.loadStreams(client);
				this.action((Frame) this.inputStream.readObject());

				client.close();
				verbose("New connection finished.", Role.SERVER);
			}
		} catch (IOException | ClassNotFoundException e) {
			this.isOn = false;
		}

		this.close();
	}

	@Override
	public void startRoutine() throws ClassNotFoundException {
		Frame frame;
		boolean nextIsUp = false;

		this.setVerbose();

		new Thread(this).start();

		while (true) {
			try {
				TimeUnit.SECONDS.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				verbose("Program error, could perform sleep", Role.CLIENT);
				return;
			}

			if (!this.token)
				continue;

			try {
				frame = this.request(Frame.Type.SEND_VALUE, this.value);
				nextIsUp = true;
			} catch (IOException e) {
				verbose("Next server is not up", Role.CLIENT);
				if (nextIsUp) {
					verbose("Updating next address", Role.CLIENT);
					if (this.moveNextAddress()) {
						verbose("Next address updated to " + this.nextAddress, Role.CLIENT);
					} else {
						verbose("There are no other nodes on the ring. Closing the program", Role.CLIENT);
						System.exit(-1);
					}
				}
				continue;
			}

			switch (frame.getType()) {
				case REPLY_OK:
					this.token = false;
					break;
				case REPLY_KO:
					break;
				default:
					return;
			}

		}
	}

	private boolean moveNextAddress() {
		if (++this.nextAddress >= INITIAL_PORT + MAX_SERVERS) {
			this.nextAddress = INITIAL_PORT;
		}

		return this.nextAddress != this.port;
	}

}
