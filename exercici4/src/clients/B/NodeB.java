package clients.B;

import models.BaseNode;
import models.FileHandler;
import models.Role;
import network.Frame;

import java.io.IOException;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class NodeB extends BaseNode {
	private FileHandler handler;

	NodeB(Role role) {
		super(role, true);
		this.handler = new FileHandler(role.toString() + ".log");
	}

	private void solveClientRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		int variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {
			variable = Integer.parseInt(action.substring(2, action.length() - 1));
			value = this.handler.getValue(variable);
			logger.print("Read variable " + variable + ": " + value);
		}

	}

	@Override
	protected void action(Frame frame) throws IOException {

		switch (frame.getType()) {

			case REQUEST_CLIENT:
				solveClientRequest((String) frame.getData());

				reply(Frame.Type.REPLY_CLIENT, true);
				break;

			case POST_AB:

				reply(Frame.Type.REPLY_BA, true);
				break;

			default:
				reply(Frame.Type.REPLY_KO);
		}
	}

}
