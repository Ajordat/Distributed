package clients.C;

import models.BaseNode;
import models.Role;
import network.Frame;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class C1 extends BaseNode {

	private C1() {
		super(Role.C1, true);
	}

	@Override
	protected void action(Frame frame) {
		System.out.println("asdf");
	}

	public static void main(String[] args) {
		C1 node = new C1();

		node.startRoutine();
	}
}
