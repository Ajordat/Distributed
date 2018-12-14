package clients.A;

import models.FileHandler;
import models.NodeRole;
import models.TransactionResults;
import network.BaseNode;
import network.Frame;
import network.WebSocketEndpoint;

import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class NodeA extends BaseNode {
	private final static int MAX_TRANSACTIONS = 2;
	private FileHandler fileHandler;
	private int transactionsCounter;
	private NodeRole lowerNode;
	private WebSocketEndpoint webSocketEndpoint;

	NodeA(NodeRole node, NodeRole lowerNode) {
		super(node, true);
		this.fileHandler = new FileHandler(node.toString() + ".log");
		this.lowerNode = lowerNode;

		this.transactionsCounter = 0;

		this.webSocketEndpoint = new WebSocketEndpoint(
				new InetSocketAddress("localhost", node.getWsPort()),
				logger
		);
		this.webSocketEndpoint.start();
	}

	NodeA(NodeRole node) {
		this(node, null);
	}

	private TransactionResults solveClientRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		StringBuilder writeTransactions = new StringBuilder();
		StringBuilder readTransactions = new StringBuilder();
		int index, variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {
			logger.debug(action);

			if (action.charAt(0) == 'w') {
				index = action.indexOf(',');
				variable = Integer.parseInt(action.substring(2, index));
				value = Integer.parseInt(action.substring(index + 1, action.length() - 1));
				logger.print("Write " + value + " on variable " + variable + ".");
				fileHandler.setValue(variable, value);

				if (!writeTransactions.toString().isEmpty())
					writeTransactions.append(',');
				writeTransactions.append(action);

			} else {
				variable = Integer.parseInt(action.substring(2, action.length() - 1));
				value = this.fileHandler.getValue(variable);
				logger.print("Read variable " + variable + ": " + value);

				if (!readTransactions.toString().isEmpty())
					readTransactions.append(',');
				readTransactions.append("r(").append(variable).append(',').append(value).append(")");
			}
		}

		return new TransactionResults(writeTransactions.toString(), readTransactions.toString());
	}

	private void notifyReplicationNodes(String writeResults) throws IOException, ClassNotFoundException {
		Frame response;

		for (NodeRole node : this.node.getBroadcastNodes()) {
			response = request(node.getPort(), Frame.Type.POST_AA, writeResults);

			if (response.getType() == Frame.Type.REPLY_AA && (boolean) response.getData())
				logger.debug("NODE " + node + " CONFIRMED");
			else
				logger.debug("NODE " + node + " DECLINED");
		}
	}

	@Override
	protected void action(Frame frame) throws IOException, ClassNotFoundException {

		switch (frame.getType()) {

			case REQUEST_CLIENT:
				TransactionResults results = solveClientRequest((String) frame.getData());

				if (results.hasWriteResults())
					this.notifyReplicationNodes(results.getWriteResult());

				reply(Frame.Type.REPLY_CLIENT, results.getReadResult());

				afterTransaction();
				break;

			case POST_AA:
				String writeTransactions = (String) frame.getData();
				logger.print(writeTransactions);
				solveClientRequest(writeTransactions);
				reply(Frame.Type.REPLY_AA, true);

				afterTransaction();
				break;

			default:
				reply(Frame.Type.REPLY_KO);
		}

	}

	private void afterTransaction() throws IOException, ClassNotFoundException {
		Frame response;

		this.transactionsCounter++;

		if (transactionsCounter >= MAX_TRANSACTIONS) {

			if (lowerNode == null)
				return;

			String msg = fileHandler.toTransaction();

			if (msg.isEmpty())
				return;

			logger.debug("Updating node " + lowerNode + " with transaction [" + msg + "]");

			response = request(lowerNode.getPort(), Frame.Type.POST_AB, msg);

			if (response.getType() == Frame.Type.REPLY_BA && (boolean) response.getData()) {
				logger.debug(lowerNode + " confirmed the transaction");
			} else {
				logger.debug(lowerNode + " denied the transaction");
			}

			this.transactionsCounter = 0;
		}
	}

}
