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
public abstract class NodeA extends BaseNode {
	private FileHandler handler;

	NodeA(Role role) {
		super(role, true);
		this.handler = new FileHandler(role.toString() + ".log");
	}

	private String solveClientRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		StringBuilder writeTransactions = new StringBuilder("b");
		int index, variable, value;
		boolean readOnly = true;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {

			if (action.charAt(0) == 'w') {
				index = action.indexOf(',');
				variable = Integer.parseInt(action.substring(2, index));
				value = Integer.parseInt(action.substring(index + 1, action.length() - 1));
				logger.print("Write " + value + " on variable " + variable + ".");
				handler.setValue(variable, value);
				writeTransactions.append(",").append(action);
				readOnly = false;
			} else {
				variable = Integer.parseInt(action.substring(2, action.length() - 1));
				value = this.handler.getValue(variable);
				logger.print("Read variable " + variable + ": " + value);
			}
		}

		return readOnly ? "" : writeTransactions + ",c";
	}

	@Override
	protected void action(Frame frame) throws IOException, ClassNotFoundException {
		Frame response;
		String writeTransactions;

		switch (frame.getType()) {
			case REQUEST_CLIENT:
				writeTransactions = solveClientRequest((String) frame.getData());

				if (!writeTransactions.isEmpty())
					for (Role node : this.role.getBroadcastNodes()) {
						response = request(node.getPort(), Frame.Type.POST_AA, writeTransactions);

						if (response.getType() == Frame.Type.REPLY_AA && ((ReplyData) response.getData()).getStatus())
							logger.debug("NODE " + node + " CONFIRMED");
						else
							logger.debug("NODE " + node + " DECLINED");
					}

				reply(Frame.Type.REPLY_CLIENT, new ReplyData(true));
				break;

			case POST_AA:
				logger.print((String) frame.getData());
				reply(Frame.Type.REPLY_AA, new ReplyData(true));
				break;
			default:
				reply(Frame.Type.REPLY_CLIENT);
		}
	}

}
