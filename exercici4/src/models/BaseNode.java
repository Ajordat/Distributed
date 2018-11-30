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
	protected Logger logger;

	public BaseNode(Role role) {
		this(role, false);
	}

	public BaseNode(Role role, boolean verbose) {
		this.role = role;
		this.port = role.getPort();
		this.logger = new Logger(verbose, role.toString());
	}

	public void startRoutine() {

		if (!this.open()) {
			logger.error("Couldn't open server on port " + role.getPort());
			System.exit(1);
		}

		this.isOn = true;

		try {
			while (this.isOn) {
				logger.debug("Waiting new connections...");
				Socket client = this.server.accept();
				logger.debug("New connection received on port " + this.port);

				this.loadStreams(client);
				this.action((Frame) this.inputStream.readObject());
				this.closeStreams(client);

				logger.debug("New connection finished.");
			}
		} catch (IOException | ClassNotFoundException e) {
			this.isOn = false;
		}

		this.close();
	}

	protected abstract void action(Frame frame) throws IOException, ClassNotFoundException;
}
