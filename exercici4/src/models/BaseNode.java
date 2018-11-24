package models;

import network.Frame;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class BaseNode extends BaseServer {
	protected Role role;
	protected Logger log;

	public BaseNode(Role role) {
		this.role = role;
		this.port = role.getPort();
		this.log = new Logger(role.toString(), false);
	}

	public BaseNode(Role role, boolean verbose) {
		this.role = role;
		this.port = role.getPort();
		this.log = new Logger(role.toString(), verbose);
	}

	protected void startRoutine() {

		if (!this.open())
			return;

		this.isOn = true;

		try {
			while (this.isOn) {
				log.verbose("Waiting new connections...");
				Socket client = this.server.accept();
				log.verbose("New connection received on port " + this.port);

				this.loadStreams(client);
				this.action((Frame) this.inputStream.readObject());

				client.close();
				log.verbose("New connection finished.");
			}
		} catch (IOException | ClassNotFoundException e) {
			this.isOn = false;
		}

		this.close();
	}

	protected abstract void action(Frame frame) throws IOException;
}
