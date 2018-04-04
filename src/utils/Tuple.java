/*
 * Released under GNU General Public License v3.0
 * @author Luca Banzato
 * @version 0.1.0
 */

package utils;

public class Tuple<X, Y> {

	private X x;
	private Y y;
	
	// A simple class which implements a tuple
	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	// getter X
	public X getX() {
		return x;
	}

	// getter Y
	public Y getY() {
		return y;
	}

	// toString
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}