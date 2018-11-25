package network;

import java.io.Serializable;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class ReplyData implements Serializable {

	private boolean status;

	public ReplyData(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return Boolean.toString(status);
	}

}
