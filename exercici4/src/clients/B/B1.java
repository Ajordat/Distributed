package clients.B;

import models.BaseNode;
import models.Role;
import network.Frame;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class B1 extends BaseNode {

	private B1() {
		super(Role.B1, true);
	}

	@Override
	protected void action(Frame frame) {
		System.out.println("asdf");
	}

	public static void main(String[] args) {
		B1 node = new B1();

		node.startRoutine();
	}
}
