package models;

import network.Frame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor base del que hereten els altres servidors.
 * Aporta els mètodes per a obrir, transmetre i registrar comunicacions entre servidors.
 *
 * @author Ajordat
 * @version 1.0
 **/
@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public abstract class BaseServer {

	protected ServerSocket server;
	protected boolean isOn;
	protected ObjectInputStream inputStream;
	protected ObjectOutputStream outputStream;
	private static final String LOOPBACK_IP = "127.0.0.1";
	private boolean verbose;
	protected Role role;
	protected int port;


	protected BaseServer() {
		this.isOn = false;
		this.inputStream = null;
		this.outputStream = null;
		this.verbose = false;
	}

	protected void setVerbose() {
		this.verbose = true;
	}

	protected void verbose(String string) {
		if (this.verbose)
			System.out.println('[' + role.toString() + "]: " + string);
	}

	protected void stdout(String string) {
		System.out.println('[' + role.toString() + "]: " + string);
	}
	protected void stderr(String string) {
		System.err.println('[' + role.toString() + "]: " + string);
	}

	protected boolean open() {
		try {
			this.server = new ServerSocket(port);
			verbose("Server started on port " + port + "...");
			return true;
		} catch (Exception e) {
			verbose("Server failed to start on port " + port + "...");
		}
		return false;
	}

	protected void close() {
		try {
			server.close();
			verbose("Server on port " + this.port + " closed.");
		} catch (IOException e) {
			verbose("Server on port " + this.port + " failed to close.");
		}
	}

	protected void loadStreams(Socket socket) throws IOException {
		this.inputStream = new ObjectInputStream(socket.getInputStream());
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		verbose("New connection established.");
	}

	protected Frame request(int address, Frame.Type type) throws IOException, ClassNotFoundException {
		return request(address, new Frame(type));
	}

	protected Frame request(int address, Frame.Type type, Object data) throws IOException, ClassNotFoundException {
		return request(address, new Frame(type, data));
	}

	protected void send(int address, Frame.Type type) throws IOException {
		send(address, new Frame(type));
	}

	protected void send(int address, Frame.Type type, Object data) throws IOException {
		send(address, new Frame(type, data));
	}


	private Frame request(int address, Frame frame) throws IOException, ClassNotFoundException {
		verbose("Attempting request on port " + address);
		Socket socket = new Socket(LOOPBACK_IP, address);
		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

		outputStream.writeObject(frame);
		Frame answer = (Frame) inputStream.readObject();
		verbose("Answer received: " + answer.getType());
		verbose("Closing socket");
		inputStream.close();
		outputStream.close();
		socket.close();
		return answer;
	}

	private void send(int address, Frame frame) throws IOException {
		verbose("Attempting request on port " + address);
		Socket socket = new Socket(LOOPBACK_IP, address);
		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

		outputStream.writeObject(frame);
		verbose("Closing socket");
		outputStream.close();
		socket.close();
	}

	protected void reply(Frame.Type type) throws IOException {
		this.outputStream.writeObject(new Frame(type));
	}

	protected void reply(Frame.Type type, Object data) throws IOException {
		this.outputStream.writeObject(new Frame(type, data));
	}

	protected void broadcast(int[] addresses, Frame.Type type, int value) throws IOException, ClassNotFoundException {
		for (int address : addresses) {
			verbose("Requesting to " + address);
			request(address, type);
		}
	}

	public void setStreams(ObjectInputStream input, ObjectOutputStream output) {
		this.inputStream = input;
		this.outputStream = output;
	}
}
