package lightweightA;

import models.Role;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class LWA2 extends LightWeightA {
	private LWA2() {
		super(Role.LWA2);
	}

	public static void main(String[] args) {
		LWA2 lwa = new LWA2();

		lwa.run();
	}
}
