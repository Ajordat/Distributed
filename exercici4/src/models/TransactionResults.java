package models;

/**
 * @author Ajordat
 * @version 1.0
 **/

public class TransactionResults {
	private String writeResult;
	private String readResult;

	public TransactionResults(String writeResult, String readResult) {
		this.writeResult = writeResult;
		this.readResult = readResult;
	}

	public String getWriteResult() {
		return writeResult;
	}

	public String getReadResult() {
		return readResult;
	}

	public boolean hasWriteResults() {
		return !writeResult.isEmpty();
	}

	public boolean hasReadResults() {
		return !readResult.isEmpty();
	}
}
