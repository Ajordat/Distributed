package clients.A;

import models.BaseNode;
import models.FileHandler;
import models.Role;
import network.Frame;

import java.io.IOException;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class NodeA extends BaseNode {
	private final static int MAX_TRANSACTIONS = 2;
	private FileHandler fileHandler;
	private int nTransactions;
	private Role lowerNode;

	NodeA(Role role, Role lowerNode) {
		super(role, true);
		this.fileHandler = new FileHandler(role.toString() + ".log");
		this.nTransactions = 0;
		this.lowerNode = lowerNode;
	}

	NodeA(Role role) {
		this(role, null);
	}

	private String solveClientRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		StringBuilder writeTransactions = new StringBuilder();
		int index, variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {

			if (action.charAt(0) == 'w') {
				index = action.indexOf(',');
				variable = Integer.parseInt(action.substring(2, index));
				value = Integer.parseInt(action.substring(index + 1, action.length() - 1));
				logger.print("Write " + value + " on variable " + variable + ".");
				fileHandler.setValue(variable, value);
				writeTransactions.append(action).append(",");

			} else {
				variable = Integer.parseInt(action.substring(2, action.length() - 1));
				value = this.fileHandler.getValue(variable);
				logger.print("Read variable " + variable + ": " + value);
			}
		}

		return writeTransactions.toString();
	}

	@Override
	protected void action(Frame frame) throws IOException, ClassNotFoundException {
		Frame response;
		String writeTransactions;

		switch (frame.getType()) {

			case REQUEST_CLIENT:
				writeTransactions = solveClientRequest((String) frame.getData());

				if (!writeTransactions.isEmpty()) {

					writeTransactions = "b," + writeTransactions + "c";

					for (Role node : this.role.getBroadcastNodes()) {
						response = request(node.getPort(), Frame.Type.POST_AA, writeTransactions);

						if (response.getType() == Frame.Type.REPLY_AA && (boolean) response.getData())
							logger.debug("NODE " + node + " CONFIRMED");
						else
							logger.debug("NODE " + node + " DECLINED");
					}
				}

				reply(Frame.Type.REPLY_CLIENT, true);
				break;

			case POST_AA:
				logger.print((String) frame.getData());
				reply(Frame.Type.REPLY_AA, true);
				break;

			default:
				reply(Frame.Type.REPLY_KO);
		}

		afterTransaction();
	}

	void afterTransaction() throws IOException, ClassNotFoundException {
		Frame response;

		if (++nTransactions >= MAX_TRANSACTIONS) {
			response = request(lowerNode.getPort(), Frame.Type.POST_AB);

			if (response.getType() == Frame.Type.REPLY_BA && (boolean) response.getData()) {
				logger.debug(lowerNode + " confirmed the transaction");
			} else {
				logger.debug(lowerNode + " denied the transaction");
			}
		}
	}
}
