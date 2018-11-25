package clients.B;

import models.BaseNode;
import models.Role;
import network.Frame;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class B2 extends BaseNode {

	private B2() {
		super(Role.B2, true);
	}

	@Override
	protected void action(Frame frame) {
		System.out.println("asdf");
	}

	public static void main(String[] args) {
		B2 node = new B2();

		node.startRoutine();
	}
}
