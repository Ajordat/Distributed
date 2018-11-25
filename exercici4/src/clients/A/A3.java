package clients.A;

import models.BaseNode;
import models.Role;
import network.Frame;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A3 extends BaseNode {

	private A3() {
		super(Role.A3, true);
	}

	@Override
	protected void action(Frame frame) {
		System.out.println("asdf");
	}

	public static void main(String[] args) {
		A3 node = new A3();

		node.startRoutine();
	}
}
