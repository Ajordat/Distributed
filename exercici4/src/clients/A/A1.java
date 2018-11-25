package clients.A;

import models.BaseNode;
import models.FileHandler;
import models.Role;
import network.Frame;
import network.ReplyData;

import java.io.IOException;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A1 extends BaseNode {
	private FileHandler handler;

	private A1() {
		super(Role.A1, false);
		this.handler = new FileHandler(Role.A1.toString() + ".log");
	}

	private void solveClientRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		boolean write;
		int index, variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {
			write = action.charAt(0) == 'w';
			if (write) {
				index = action.indexOf(',');
				variable = Integer.parseInt(action.substring(2, index));
				value = Integer.parseInt(action.substring(index + 1, action.length() - 1));
				logger.print("Write " + value + " on variable " + variable + ".");
				this.handler.setValue(variable, value);
			} else {
				variable = Integer.parseInt(action.substring(2, action.length() - 1));
				logger.print("Read variable " + variable + ": " + this.handler.getValue(variable));
			}
		}

	}

	@Override
	protected void action(Frame frame) throws IOException {
		switch (frame.getType()) {
			case REQUEST_CLIENT:
				solveClientRequest((String) frame.getData());
				reply(Frame.Type.REPLY_CLIENT, new ReplyData(true));
				break;
			case POST_AA:

				break;
			default:
				reply(Frame.Type.REPLY_CLIENT);
		}
	}

	public static void main(String[] args) {
		A1 node = new A1();

		node.startRoutine();
	}
}
