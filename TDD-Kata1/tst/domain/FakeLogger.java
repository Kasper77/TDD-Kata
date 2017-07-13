package domain;

import java.util.ArrayList;
import java.util.List;

public class FakeLogger implements ILogger {

	private List<String> logs = new ArrayList<>();

	@Override
	public void write(String line) {
		logs.add(line);
	}

	@Override
	public String readLastLoggedLine() {
		return logs.get(logs.size() - 1);
	}

	@Override
	public List<String> readAllLogs() {
		return null;
	}
}
