package clients.C;

import models.BaseNode;
import models.FileHandler;
import models.NodeRole;
import network.Frame;

import java.io.IOException;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class NodeC extends BaseNode {
	private FileHandler fileHandler;

	NodeC(NodeRole node) {
		super(node, true);
		this.fileHandler = new FileHandler(node.toString() + ".log");
	}

	private String solveReadRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		StringBuilder readTransactions = new StringBuilder();
		int variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {

			variable = Integer.parseInt(action.substring(2, action.length() - 1));
			value = this.fileHandler.getValue(variable);
			logger.print("Read variable " + variable + ": " + value);

			if (!readTransactions.toString().isEmpty())
				readTransactions.append(',');
			readTransactions.append("r(").append(variable).append(',').append(value).append(")");
		}

		return readTransactions.toString();
	}

	private void solveWriteRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		int index, variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {
			logger.debug(action);

			index = action.indexOf(',');
			variable = Integer.parseInt(action.substring(2, index));
			value = Integer.parseInt(action.substring(index + 1, action.length() - 1));
			logger.print("Write " + value + " on variable " + variable + ".");
			fileHandler.setValue(variable, value);
		}
	}

	@Override
	protected void action(Frame frame) throws IOException {

		switch (frame.getType()) {

			case REQUEST_CLIENT:
				String readTransactions = solveReadRequest((String) frame.getData());
				reply(Frame.Type.REPLY_CLIENT, readTransactions);
				break;

			case POST_BC:
				logger.debug("Received [" + frame.getData() + "]");
				reply(Frame.Type.REPLY_CB, true);
				solveWriteRequest((String) frame.getData());
				break;

			default:
				reply(Frame.Type.REPLY_KO);
		}
	}

}
