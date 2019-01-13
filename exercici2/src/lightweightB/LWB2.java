package lightweightB;

import models.Role;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class LWB2 extends LightWeightB {
	private LWB2() {
		super(Role.LWB2);
	}

	public static void main(String[] args) {
		LWB2 lwa = new LWB2();

		lwa.run();
	}
}
