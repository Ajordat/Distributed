package dynamicServer;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class ReadServer extends DynamicServer {

	@Override
	public void updateCurrentValue(int value) {
		this.value = value;
		verbose("Current value is: " + this.value, Role.CLIENT);
	}

	public static void main(String[] args) throws ClassNotFoundException {
		(new ReadServer()).startRoutine();
	}

}
