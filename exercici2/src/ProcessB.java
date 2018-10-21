import models.BaseServer;
import models.Role;
import network.Frame;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class ProcessB extends BaseServer {

	private int nFinished;

	private ProcessB() {
		this.role = Role.HWB;
		this.port = Role.HWB.getPort();
	}

	private void startLWProcesses() {
		(new Thread(new LightWeightB(Role.LWB1))).start();
		(new Thread(new LightWeightB(Role.LWB2))).start();
	}

	private void action(Frame frame) throws IOException, ClassNotFoundException {

		switch (frame.getType()) {
			case TOKEN_AB:
				// Receive the token from HWA and answer with REPLY_OK
				this.reply(Frame.Type.REPLY_OK);

				// Set the number of finished thread jobs to 0 and notify all child threads that they have the token
				this.nFinished = 0;
				this.notifyLWThreads();

				return;

			case RETURN_TOKEN:

				this.reply(Frame.Type.REPLY_OK);

				// After receiving a RETURN_TOKEN, increase the number of thread jobs done and compare it with the total
				if (++this.nFinished == role.getLightWeightCount()) {

					// If all children have finished using the shared resource, give the token back to HWA
					this.request(Role.HWA.getPort(), Frame.Type.TOKEN_BA);
				}

				return;

			default:
				stderr("I have received the following frame: " + frame.getData());
		}
	}

	private void notifyLWThreads() throws IOException, ClassNotFoundException {
		Frame f1, f2;

		f1 = request(Role.LWB1.getPort(), Frame.Type.SEND_TOKEN);
		f2 = request(Role.LWB2.getPort(), Frame.Type.SEND_TOKEN);

		if (f1.isKO() || f2.isKO())
			throw new IOException("A LightWeight thread is not active.");
	}

	private void startRoutine() {

		if (!this.open())
			return;

		this.startLWProcesses();

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

		ProcessB pB = new ProcessB();

		//pB.setVerbose();
		pB.startRoutine();
	}
}
