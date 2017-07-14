package domain;

import java.util.Observable;
import java.util.Observer;

public class ConsoleView implements Observer{

	public void show (int result) {
		System.out.println(result);
	}

	@Override
	public void update(Observable o, Object arg) {
		StringCalculator sc = (StringCalculator) o;
		show(sc.getResult());
	}
}
