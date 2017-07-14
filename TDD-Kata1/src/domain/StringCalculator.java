package domain;

import java.util.Observable;

import com.sun.swing.internal.plaf.synth.resources.synth;

public class StringCalculator extends Observable {

	private int sumLimit = Integer.MAX_VALUE;
	private StringUtil string = new StringUtil();
	private ILogger logger;
	private IWebservice webservice;
	private int result;

	public StringCalculator(int sumLimit) {
		this.sumLimit  = sumLimit;
	}

	public int sum(String input) throws StringCalculatorException {
		if (input.isEmpty())
			return 0;
		result = makeSum(string.split(input));

		setChanged();
		notifyObservers();
		return result; 
	}

	public int getResult () {
		return this.result;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	private int makeSum(String[] vals) throws StringCalculatorException {
		int sum = 0;
		for (String v: vals)
			sum += increment(v);
		log(sum);
		return sum;
	}

	private void log(int sum) {
		if (logger != null)
			logger.write(Integer.toString(sum));
	}

	private int increment(String v) throws StringCalculatorException {
		int parsed = Integer.parseInt(removeSpaces(v));
		if (exceedLimit(parsed))
			return 0;
		if (negative(parsed))
			raiseException();

		return parsed;
	}

	private void raiseException() throws StringCalculatorException {
		if (webservice != null)
			webservice.report("negatives not allowed!");
		throw new StringCalculatorException ("negatives not allowed!");
	}

	private String removeSpaces(String v) {
		return v.trim();
	}

	private boolean negative(int parsed) {
		return parsed < 0;
	}

	private boolean exceedLimit(int parsed) {
		return parsed > sumLimit;
	}

	public void setWebService(IWebservice webservice) {
		this.webservice = webservice;
	}
}
