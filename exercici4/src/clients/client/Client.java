package clients.client;

import models.BaseServer;
import models.Logger;
import models.NodeRole;
import network.Frame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class Client extends BaseServer {
	private String filename;
	private Logger logger;

	private Client(String filename) {
		this.filename = filename;
		this.logger = new Logger(false, "CLI");
	}

	private NodeRole decideNode(int layer) {
		int pos;

		if (layer == 0)
			pos = ThreadLocalRandom.current().nextInt(0, 3);
		else if (layer == 1)
			pos = ThreadLocalRandom.current().nextInt(3, 5);
		else
			pos = ThreadLocalRandom.current().nextInt(5, 7);

		return NodeRole.getArray()[pos];
	}

	private void startFileTransactions() {
		Scanner scanner = null;
		NodeRole targetNode;
		String line, transaction, target;

		try {
			scanner = new Scanner(new File(this.filename));
		} catch (FileNotFoundException e) {
			logger.error("Couldn't find file \"" + this.filename + "\".\nExiting.");
			System.exit(1);
		}

		while(scanner.hasNextLine()) {
			line = scanner.nextLine();

			logger.debug(line);

			int index = line.indexOf(',');
			try {
				transaction = line.substring(index + 1, line.length() - 2);
			} catch (StringIndexOutOfBoundsException e) {
				logger.error("Invalid transaction");
				continue;
			}
			target = line.substring(1, index);

			targetNode = decideNode(target.isEmpty() ? 0 : Integer.parseInt(target));

			try {
				logger.debug("Sending frame to " + targetNode);
				Frame response = this.request(targetNode.getPort(), Frame.Type.REQUEST_CLIENT, transaction);
				logger.debug("Frame received");
				logger.debug(response.getData().toString());

				printTransactionResults(targetNode, transaction, response.getData().toString());

				Thread.sleep(500);
			} catch (IOException | ClassNotFoundException e) {
				logger.error("Couldn't reach node " + targetNode + ".");
			} catch (InterruptedException e) {
				logger.error("Interrupted sleep.");
			}
		}

		scanner.close();
	}

	private void printTransactionResults(NodeRole target, String transaction, String response) {
		String[] actions = transaction.split(",(?![^(]*\\))");
		String[] results = response.split(",(?![^(]*\\))");
		int i = 0, index, variable, value;

		logger.print("Requesting node " + target);
		for (String action : actions) {
			if (action.charAt(0) == 'w') {
				index = action.indexOf(',');
				variable = Integer.parseInt(action.substring(2, index));
				value = Integer.parseInt(action.substring(index + 1, action.length() - 1));

				logger.print("Variable " + variable + " updated to " + value);
			} else {
				index = results[i].indexOf(',');
				variable = Integer.parseInt(results[i].substring(2, index));
				value = Integer.parseInt(results[i].substring(index + 1, results[i].length() - 1));

				logger.print("Variable " + variable + " has value " + value);

				i++;
			}
		}
		logger.printString("---------------------------------");
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Missing transactions file.");
			System.exit(1);
		}

		Client c = new Client(args[0]);

		c.startFileTransactions();
	}
}
