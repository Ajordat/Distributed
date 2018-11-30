package clients.B;

import models.Role;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class B2 extends NodeB {

	private B2() {
		super(Role.B2);
	}

	public static void main(String[] args) {
		NodeB node = new B2();

		node.startRoutine();
	}
}
