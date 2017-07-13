package domain;

import java.util.List;

public interface ILogger {
	void write(String line);
	String readLastLoggedLine();
	List<String> readAllLogs();
}
