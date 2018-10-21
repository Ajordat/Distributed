package utils;

import java.util.Random;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class ArrayFactory {
	public enum Method {RANDOM, ASCENDING, DESCENDING, CONSTANT}

	public static int[] create(int size, Method method, int offset) {
		int[] numbers = new int[size];

		switch (method) {
			case RANDOM:
				Random random = new Random();
				for (int i = 0; i < size; i++)
					numbers[i] = random.nextInt(offset + 1);
				break;

			case ASCENDING:
				for (int i = 0; i < size; i++)
					numbers[i] = offset + i;
				break;

			case DESCENDING:
				for (int i = 0; i < size; i++)
					numbers[i] = offset + size - i - 1;
				break;

			case CONSTANT:
				for (int i = 0; i < size; i++)
					numbers[i] = offset;
				break;
		}

		return numbers;
	}

}
