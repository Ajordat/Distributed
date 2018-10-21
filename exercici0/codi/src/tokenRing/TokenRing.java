package tokenRing;

import models.BaseServer;
import network.Frame;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static network.Frame.Type.REPLY_INSERT;
import static network.Frame.Type.REQUEST_INSERT;

/**
 * Classe que implementa un node de la topologia token ring.
 *
 * @author Ajordat
 * @version 1.0
 **/
public abstract class TokenRing extends BaseServer {

	private boolean token;
	private static final int MAX_INTENTS = 5;

	TokenRing() {
		int turn = this.getTurn("turn.txt");

		if (turn < 0) {
			verbose("There's no turn available", Role.HOST);
			System.exit(-1);
		}

		this.port = INITIAL_PORT + turn;
		this.token = (turn == 0);
		this.nextAddress = INITIAL_PORT;
	}

	/**
	 * Inicia la rutina del node.
	 * @throws ClassNotFoundException Quan falla una petició a un servidor.
	 */
	@Override
	protected void startRoutine() throws ClassNotFoundException {

		Frame frame;
		boolean hasConnected = false;

		this.setVerbose();

		if (!this.token) {
			if (!this.requestRingPlace()) {
				verbose("There's no more room for this host.", Role.HOST);
				return;
			} else {
				verbose("My next address is " + this.nextAddress, Role.HOST);
			}
		} else {
			verbose("I'm the first node.", Role.HOST);
		}

		new Thread(this).start();

		while (true) {
			try {
				TimeUnit.SECONDS.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				verbose("Program error, could perform sleep", Role.HOST);
				return;
			}

			if (!this.token)
				continue;

			if (this.port == INITIAL_PORT && this.nextAddress == INITIAL_PORT) {
				verbose("Waiting for a new host...", Role.CLIENT);
				continue;
			}

			try {
				frame = this.request(Frame.Type.SEND_VALUE, this.value);
				hasConnected = true;
			} catch (IOException e) {
				verbose("Next server is not up", Role.CLIENT);
				if (hasConnected) {
					verbose("Trying to reconnect to another node.", Role.CLIENT);
					if (!this.reconnectToRing()) {
						verbose("Couldn't restore connection with a new server. Exiting.", Role.CLIENT);
						return;
					} else
						verbose("My next address is " + this.nextAddress, Role.HOST);
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

	/**
	 * Rutina a realitzar quan es rep una connexió al servidor.
	 *
	 * @param frame Frame rebut.
	 * @throws IOException Quan falla la transmissió d'un missatge.
	 */
	@Override
	protected void action(Frame frame) throws IOException {
		Frame frameOut;
		int data;

		switch (frame.getType()) {
			case REQUEST_INSERT:
				data = (Integer) frame.getData();

				if (data >= 0) {
					frameOut = new Frame(REPLY_INSERT, this.nextAddress);
					this.nextAddress = data;

				} else
					frameOut = new Frame(REPLY_INSERT, -1);

				this.outputStream.writeObject(frameOut);

				break;

			case SEND_VALUE:
				data = (Integer) frame.getData();

				if (data >= 0) {
					frameOut = new Frame(Frame.Type.REPLY_OK, data);
					this.updateCurrentValue(data);
					this.token = true;
				} else
					frameOut = new Frame(Frame.Type.REPLY_KO, data);

				this.outputStream.writeObject(frameOut);

				break;

			default:
				break;
		}
	}

	/**
	 * Rutina del servidor. Executada en paral·lel al implementar Runnable.
	 * Quan rep una connexió, crida a action().
	 */
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

	/**
	 * Sol·licita un lloc a la xarxa creada.
	 *
	 * @return Si s'ha aconseguit trobar un lloc.
	 * @throws ClassNotFoundException Quan falla una connexió.
	 */
	private boolean requestRingPlace() throws ClassNotFoundException {

		for (int intents = 0; intents < MAX_INTENTS; intents++) {
			try {
				Frame frame = this.request(REQUEST_INSERT, this.port);
				if (frame.getType() == REPLY_INSERT && (Integer) frame.getData() >= 0) {
					this.nextAddress = (Integer) frame.getData();
					return true;
				}
				return false;
			} catch (IOException e) {
				this.nextAddress++;
				if (this.nextAddress < INITIAL_PORT) {
					this.nextAddress = INITIAL_PORT + MAX_INTENTS;
				}
				if (this.nextAddress == this.port)
					this.nextAddress++;
			}
		}

		return false;
	}

	/**
	 * Intenta reconnectar a un nou node després de que el node següent hagi caigut.
	 * @return Si ha aconseguit trobar lloc a la xarxa.
	 * @throws ClassNotFoundException Quan falla una connexió.
	 */
	private boolean reconnectToRing() throws ClassNotFoundException {

		if (--this.nextAddress < INITIAL_PORT)
			this.nextAddress = INITIAL_PORT + MAX_INTENTS;

		for (int intents = 0; intents < MAX_INTENTS; intents++) {
			try {
				Frame frame = this.request(REQUEST_INSERT, -1);
				return frame.getType() == REPLY_INSERT;
			} catch (IOException e) {
				if (--this.nextAddress < INITIAL_PORT)
					this.nextAddress = INITIAL_PORT + MAX_INTENTS;
				if (this.nextAddress == this.port)
					this.nextAddress--;
			}
		}

		return false;
	}

}
