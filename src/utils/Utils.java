/*
 * Released under GNU General Public License v3.0
 * @author Luca Banzato
 * @version 0.1.0
 */

package utils;

import java.util.ArrayList;

public class Utils {

	// This function prints an ArrayList
	public static String printAL(ArrayList<?> array) {
		StringBuilder result = new StringBuilder("[");
		int i;
		for (i = 0; i < array.size() - 1; i++) {
			result.append(array.get(i) + ", ");
		}
		result.append(array.get(i));
		result.append("]");
		return result.toString();
	}

	// This function repeats a char N times
	public static void repeatChar(char ch, int times) {
		for (int i = 0; i < times; i++) {
			System.out.print(ch);
		}
		System.out.print('\n');
	}
}
