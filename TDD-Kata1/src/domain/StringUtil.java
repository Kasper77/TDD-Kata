package domain;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	private static final int END_OF_LINE_LENGTH = 1;
	private static final String END_OF_LINE = "\n";
	private static final int MULTI_DELIMITER_LENGTH = 2;
	private static final String MULTI_DELIMITER = "//";

	public StringUtil() {
	}

	public String[] split(String input) {
		if (multiDelimiter(input)) {
			return splitMultiDelimiter(input);
		} else
			return splitSingleDelimiter(input);
	}

	private String[] splitSingleDelimiter(String input) {
		return input.split("[,|\n]");
	}

	private boolean multiDelimiter(String input) {
		return input.startsWith(MULTI_DELIMITER);
	}

	private String[] splitMultiDelimiter(String input) {
		String delimiter = extractDelimiter(input);
		delimiter = removeAnySquareParenthesis(delimiter);
		String newInput = extractData(input);
		List<String> ret = new ArrayList<String>();
		int i = 0;
		String tempString = newInput.substring(0, newInput.length());
		while (i < newInput.length()) {
			int nextDelimiterIndex = tempString.indexOf(delimiter);
			nextDelimiterIndex = (nextDelimiterIndex != -1) ? nextDelimiterIndex : tempString.length();
			ret.add(tempString.substring(0, nextDelimiterIndex));
			i += delimiter.length() + 1;
			if (!endOfString(delimiter, tempString))
				tempString = nextSubstringToParse(delimiter, tempString, nextDelimiterIndex);
		}
		return ret.toArray(new String[ret.size()]);
	}

	private boolean endOfString(String delimiter, String tempString) {
		return tempString.indexOf(delimiter) == -1;
	}

	private String nextSubstringToParse(String delimiter, String tempString, int nextDelimiterIndex) {
		return tempString.substring(nextDelimiterIndex+delimiter.length(), tempString.length());
	}

	private String extractData(String input) {
		return input.substring(input.indexOf(END_OF_LINE) + END_OF_LINE_LENGTH, input.length());
	}

	private String removeAnySquareParenthesis(String delimiter) {
		return delimiter.replaceAll("[\\[|\\]]", "");
	}

	private String extractDelimiter(String input) {
		return input.substring(input.indexOf(MULTI_DELIMITER) + MULTI_DELIMITER_LENGTH, input.indexOf(END_OF_LINE));
	}
}