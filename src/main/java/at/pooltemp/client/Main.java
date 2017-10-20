package at.pooltemp.client;

import at.pooltemp.client.service.temperature.TemperatureController;

public class Main {

	public static void main(String[] args) {
		new TemperatureController().start();
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
