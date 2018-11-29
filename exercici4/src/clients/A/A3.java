package clients.A;

import models.Role;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A3 extends NodeA {

	private A3() {
		super(Role.A3);
	}

	public static void main(String[] args) {
		NodeA node = new A3();

		node.startRoutine();
	}
}
