package dynamicServer;

import models.BaseServer;
import network.Frame;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class DynamicServer extends BaseServer {

	private boolean isMaster;

	DynamicServer() {
		this.nextAddress = INITIAL_PORT;

		int turn = this.getTurn("turn.txt");

		this.port = turn >= 0 ? INITIAL_PORT + turn : -1;

		this.isMaster = (turn == 0);
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
	protected void startRoutine() throws ClassNotFoundException {
		Frame frame;
		this.setVerbose();

		new Thread(this).start();

		while (true) {
			try {
				TimeUnit.SECONDS.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				verbose("Program error, could perform sleep", Role.CLIENT);
				return;
			}

			synchronized (this) {
				if (this.isMaster)
					continue;
				try {
					frame = this.request(Frame.Type.REQUEST_VALUE, this.nextAddress);
				} catch (IOException e) {
					verbose("Next server is not up", Role.CLIENT);
					if (this.nextAddress == INITIAL_PORT || this.port == INITIAL_PORT) {
						verbose("Couldn't connect to original server. Exiting", Role.CLIENT);
						return;
					}
					verbose("Attempting to reconnect with original server.", Role.CLIENT);
					this.nextAddress = INITIAL_PORT;
					continue;
				}
			}

			switch (frame.getType()) {

				case REPLY_VALUE:
					this.updateCurrentValue((Integer) frame.getData());
					this.isMaster = true;
					break;

				case REPLY_ADDRESS:
					this.nextAddress = (Integer) frame.getData();
					break;

				default:
					return;
			}
		}
	}

	@Override
	protected void action(Frame frame) throws IOException {
		switch (frame.getType()) {
			case REQUEST_VALUE:
				Frame frameOut;
				if (this.isMaster)
					frameOut = new Frame(Frame.Type.REPLY_VALUE, this.value);
				else
					frameOut = new Frame(Frame.Type.REPLY_ADDRESS, this.nextAddress);

				synchronized (this) {
					this.outputStream.writeObject(frameOut);
					this.isMaster = false;
					this.nextAddress = (Integer) frame.getData();
				}
				break;

			default:
				break;
		}
	}

}
