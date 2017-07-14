package domain;
import static org.junit.Assert.assertTrue;

import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class StringCalculatorSpec {

	private static final int SUM_LIMIT = 1000;
	@Tested StringCalculator unit;

	@Before
	public void setUp() {
		unit = new StringCalculator(SUM_LIMIT);
	}

	@Test
	public void sum_whenEmptyInput() throws Exception {
		String input = "";

		assertTrue(unit.sum(input) == 0);
	}

	@Test
	public void sum_whenJustOneNumber() throws Exception {
		String input = "1";

		assertTrue(unit.sum(input) == Integer.parseInt(input));
	}

	@Test
	public void sum_whenTwoNumbersWithCommaDelimiter() throws Exception {
		String input = "1, 5";
		int expectedOutput = 6;

		assertTrue(unit.sum(input) == expectedOutput);
	}

	@Test
	public void sum_whenTwoNumbersWithMixedDelimiter() throws Exception {
		String input = "1, 5\n8";
		int expectedOutput = 14;

		assertTrue(unit.sum(input) == expectedOutput);
	}

	@Test(expected = StringCalculatorException.class)
	public void sum_whenANegativeNumberPassed() throws Exception {
		String input = "-1";
		unit.sum(input);
	}

	@Test(expected = StringCalculatorException.class)
	public void sum_whenNegativeNumberPassedWithOthers() throws Exception {
		String input = "1, 5, -1";
		unit.sum(input);
	}

	@Test
	public void sum_whenANumberOverLimitIsPassed() throws Exception {
		String input = "1001";
		assertTrue(unit.sum(input) == 0);
	}

	@Test
	public void sum_whenNumberOverLimitIsPassed() throws Exception {
		String input = "1, 5, 15\n1001";
		assertTrue(unit.sum(input) == 21);
	}

	@Test
	// //[delimiter]\n[numbers…]
	public void sum_whenDifferentDelimiter() throws Exception {
		String input = "//;\n1;2;5";
		assertTrue(unit.sum(input) == 8);
	}

	@Test
	public void sum_whenDifferentMultiDelimiterStar() throws Exception {
		String input = "//[***]\n1***2***3";
		assertTrue(unit.sum(input) == 6);
	}

	@Test
	public void sum_whenDifferentMultiDelimiterDash() throws Exception {
		String input = "//[--]\n1--5--10";
		assertTrue(unit.sum(input) == 16);
	}

	@Test
	public void sum_whenDifferentMultiDelimiterDashWithBigNumbers() throws Exception {
		String input = "//[--]\n30--100--320--1001";
		assertTrue(unit.sum(input) == 450);
	}

	@Test
	public void sum_whenValidResultLogged() throws Exception {
		String input = "//[***]\n1***2***3";
		ILogger logger = new FakeLogger();
		unit.setLogger(logger);

		assertTrue(unit.sum(input) == 6);
		assertTrue(logger.readLastLoggedLine().equals("6"));
	}

	@Test(expected = StringCalculatorException.class)
	public void sum_whenValidResultLoggedOnWebservice(@Injectable IWebservice mockedWebservice) throws Exception {
		String input = "//[***]\n-1";
		new Expectations() {{ mockedWebservice.report("negatives not allowed!"); times = 1;}};

		unit.sum(input);
	}

	@Test
	public void sum_whenTipicalOutputToConsole(@Mocked ConsoleView mockedConsoleView) throws Exception {
		String input = "//[***]\n1***2***5";
		new Expectations() {{ mockedConsoleView.update( (Observable) withNotNull(), any); times = 1;}};
		unit.addObserver(mockedConsoleView);

		assertTrue(unit.sum(input) == 8);
	}
}
