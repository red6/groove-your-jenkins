package de.ppi.sf1.experimental.cdexample.webapp;

import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {

	@Test
	public void shouldAddArgToResult() {
		
		final Calculator testee = new Calculator();
		
		testee.add(5);
		
		Assert.assertEquals(5, testee.getResult());
	}

}
