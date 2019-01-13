package lightweightA;

import models.Role;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class LWA3 extends LightWeightA {
	private LWA3() {
		super(Role.LWA3);
	}

	public static void main(String[] args) {
		LWA3 lwa = new LWA3();

		lwa.run();
	}
}
