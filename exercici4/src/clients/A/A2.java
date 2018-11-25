package clients.A;

import models.BaseNode;
import models.Role;
import network.Frame;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A2 extends BaseNode {

	private A2() {
		super(Role.A2, true);
	}

	@Override
	protected void action(Frame frame) {
		System.out.println("asdf");
	}

	public static void main(String[] args) {
		A2 node = new A2();

		node.startRoutine();
	}
}
