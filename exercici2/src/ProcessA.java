import models.BaseServer;
import models.Role;
import network.Frame;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class ProcessA extends BaseServer {

	private int nFinished;

	private ProcessA() {
		this.role = Role.HWA;
		this.port = Role.HWA.getPort();
	}

	private void action(Frame frame) throws IOException, ClassNotFoundException {

		switch (frame.getType()) {
			case TOKEN_BA:
				// Receive the token from HWB and answer with REPLY_OK
				this.reply(Frame.Type.REPLY_OK);

				// Set the number of finished thread jobs to 0 and notify all child threads that they have the token
				this.nFinished = 0;
				this.notifyLWThreads();

				return;

			case RETURN_TOKEN:

				this.reply(Frame.Type.REPLY_OK);

				// After receiving a RETURN_TOKEN, increase the number of thread jobs done and compare it with the total
				if (++this.nFinished == role.getLightWeightCount()) {

					// If all children have finished using the shared resource, give the token back to HWB
					this.request(Role.HWB.getPort(), Frame.Type.TOKEN_AB);
				}

				return;

			default:
				stderr("I have received the following frame: " + frame.getData());
		}
	}

	private void notifyLWThreads() throws IOException, ClassNotFoundException {
		Frame f1, f2, f3;

		f1 = request(Role.LWA1.getPort(), Frame.Type.SEND_TOKEN);
		f2 = request(Role.LWA2.getPort(), Frame.Type.SEND_TOKEN);
		f3 = request(Role.LWA3.getPort(), Frame.Type.SEND_TOKEN);

		if (f1.isKO() || f2.isKO() || f3.isKO())
			throw new IOException("A LightWeight thread is not active.");
	}

	private void startRoutine() {

		if (!this.open())
			return;

		this.isOn = true;

		try {
			this.notifyLWThreads();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			this.close();
			this.isOn = false;
			return;
		}

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

		ProcessA pA = new ProcessA();

		//pA.setVerbose();
		pA.startRoutine();
	}
}
