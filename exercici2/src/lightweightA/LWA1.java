package lightweightA;

import models.Role;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class LWA1 extends LightWeightA {
	private LWA1() {
		super(Role.LWA1);
	}

	public static void main(String[] args) {
		LWA1 lwa = new LWA1();

		lwa.run();
	}
}
