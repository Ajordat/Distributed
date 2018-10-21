import models.BaseServer;
import models.CentMutex;
import models.Role;
import network.Frame;
import network.LamportData;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class LightWeightA extends BaseServer implements Runnable {

	private CentMutex mutex;

	LightWeightA(Role role) {
		this.role = role;
		this.port = role.getPort();
		this.setVerbose();
		this.mutex = new CentMutex(role);
	}

	private void action(Frame frame) throws IOException {
		verbose("Received request with header " + frame.getType());

		switch (frame.getType()) {
			case SEND_TOKEN:
				this.reply(Frame.Type.REPLY_OK);

				(new Thread(() -> {
					try {
						mutex.requestCS();
						for (int i = 0; i < 5; i++) {
							stdout("I'm " + role);
							Thread.sleep(1000);
						}

						mutex.releaseCS();

						Frame ans = this.request(Role.HWA.getPort(), Frame.Type.RETURN_TOKEN);
						if (ans.getType() == Frame.Type.REPLY_KO)
							throw new IOException("HeavyWeight server couldn't process the petition.");

					} catch (IOException | ClassNotFoundException | InterruptedException e) {
						e.printStackTrace();
					}

				})).start();

				return;

			case REQUEST_CS:
			case RELEASE_CS:
				LamportData data = (LamportData) frame.getData();
				mutex.setStreams(inputStream, outputStream);
				mutex.handleMsg(frame.getType(), data.getData(), data.getSrc());
				return;

			default:
				stderr("I have received the following frame: " + frame.getData());
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
				verbose("New connection received on port " + this.port);

				this.loadStreams(client);
				this.action((Frame) this.inputStream.readObject());

				client.close();
				verbose("New connection finished.");
			}
		} catch (IOException | ClassNotFoundException e) {
			this.isOn = false;
			e.printStackTrace();
		}

		this.close();
	}

}
