package clients.C;

import models.BaseServer;
import models.Role;
import network.Frame;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class C2 extends BaseServer {

	private C2() {
		this.role = Role.C2;
		this.port = Role.C2.getPort();
	}


	private void startRoutine() {

		if (!this.open())
			return;

		this.isOn = true;

		try {
			while (this.isOn) {
				verbose("Waiting new connections...");
				Socket client = this.server.accept();
				verbose("New connection received on port " + this.port);

				this.loadStreams(client);
				this.action((Frame) this.inputStream.readObject());

				client.close();
				verbose("New connection finished.");
			}
		} catch (IOException | ClassNotFoundException e) {
			this.isOn = false;
		}

		this.close();
	}

	public static void main(String[] args) {
		C2 node = new C2();

		node.startRoutine();
	}
}
