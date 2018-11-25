package clients.C;

import models.BaseNode;
import models.Role;
import network.Frame;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class C2 extends BaseNode {

	private C2() {
		super(Role.C2, true);
	}

	@Override
	protected void action(Frame frame) {
		System.out.println("asdf");
	}

	public static void main(String[] args) {
		C2 node = new C2();

		node.startRoutine();
	}
}
