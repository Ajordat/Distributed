package models;

import network.Frame;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class BaseNode extends BaseServer {
	protected Node node;
	protected Logger logger;

	public BaseNode(Node node) {
		this(node, false);
	}

	public BaseNode(Node node, boolean verbose) {
		this.node = node;
		this.port = node.getPort();
		this.logger = new Logger(verbose, node.toString());
	}

	public void startRoutine() {

		if (!this.open()) {
			logger.error("Couldn't open server on port " + node.getPort());
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
			logger.error("Server failure on port " + node.getPort());
			System.exit(1);
		}

		this.close();
	}

	protected abstract void action(Frame frame) throws IOException, ClassNotFoundException;
}
