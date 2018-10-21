package models;

import network.Frame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor base del que hereten els altres servidors.
 * Aporta els mètodes per a obrir, transmetre i registrear comunicacions entre servidors.
 *
 * @author Ajordat
 * @version 1.0
 **/
public abstract class BaseServer implements Runnable, StartFromFile {

	protected int port;
	protected ServerSocket server;
	protected boolean isOn;
	protected int value;
	protected int nextAddress;
	protected ObjectInputStream inputStream;
	protected ObjectOutputStream outputStream;
	protected static final int INITIAL_PORT = 3000;
	private static final String LOOPBACK_IP = "127.0.0.1";
	protected int SLEEP_TIME = 1;
	private boolean verbose;

	public enum Role {
		SERVER, CLIENT, HOST;

		public char getHeader() {
			switch (this) {
				case SERVER: return 'S';
				case CLIENT: return 'C';
				case HOST: return 'H';
			}
			return 'U';
		}
	}


	protected BaseServer() {
		this.isOn = false;
		this.value = 0;
		this.inputStream = null;
		this.outputStream = null;
		this.verbose = false;
	}

	protected void setVerbose() {
		this.verbose = true;
	}

	protected void verbose(String string, Role role) {
		if (this.verbose)
			System.out.println(role.getHeader() + "[" + (this.port - INITIAL_PORT) + "]: " + string);
	}

	protected boolean open() {
		try {
			this.server = new ServerSocket(port);
			verbose("Server started on port " + port + "...", Role.SERVER);
			return true;
		} catch (Exception e) {
			verbose("Server failed to start on port " + port + "...", Role.SERVER);
		}
		return false;
	}

	protected void close() {
		try {
			server.close();
			verbose("Server on port " + this.port + " closed.", Role.SERVER);
		} catch (IOException e) {
			verbose("Server on port " + this.port + " failed to close.", Role.SERVER);
		}
	}

	protected void loadStreams(Socket socket) throws IOException {
		this.inputStream = new ObjectInputStream(socket.getInputStream());
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		verbose("New connection established.", Role.SERVER);
	}


	protected Frame request(Frame.Type type, int value) throws IOException, ClassNotFoundException {
		verbose("Attempting request on port " + this.nextAddress, Role.CLIENT);
		Socket socket = new Socket(LOOPBACK_IP, this.nextAddress);
		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

		outputStream.writeObject(new Frame(type, value));
		Frame frame = (Frame) inputStream.readObject();
		verbose("Answer received: " + frame.getType(), Role.CLIENT);
		socket.close();
		verbose("Closing socket", Role.CLIENT);
		return frame;
	}

	protected abstract void startRoutine() throws ClassNotFoundException;

	protected abstract void action(Frame frame) throws IOException;

	public abstract void updateCurrentValue(int value);

}
