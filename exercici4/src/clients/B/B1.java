package clients.B;

import models.Role;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class B1 extends NodeB {

	private B1() {
		super(Role.B1);
	}

	public static void main(String[] args) {
		NodeB node = new B1();

		node.startRoutine();
	}
}
