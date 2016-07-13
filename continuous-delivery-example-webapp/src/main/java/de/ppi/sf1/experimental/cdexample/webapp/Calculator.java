package de.ppi.sf1.experimental.cdexample.webapp;

public class Calculator {

	private int result = 0;

	public void add(int arg) {
		result = result + arg;
	}
	
	public int getResult() {
		return result;
	}
	
}
