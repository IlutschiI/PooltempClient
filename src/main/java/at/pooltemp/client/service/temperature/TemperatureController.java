package at.pooltemp.client.service.temperature;


import java.util.List;

import at.pooltemp.client.http.HTTPTemperatureRequest;
import at.pooltemp.client.logger.Logger;
import at.pooltemp.client.properties.PropertyFinder;
import at.pooltemp.client.service.temperature.db.TemperatureDBFacade;
import at.pooltemp.client.service.temperature.model.Temperature;

public class TemperatureController {

	//private static final int MINS30_IN_MILLISECONDS = 1800000;
	private static final int TIME_BETWEEN_MEASUREMENTS = Integer.valueOf(PropertyFinder.getInstance().findProperty("time_between_measurements"));
	//private static final int MINS30_IN_MILLISECONDS = 60000;

	private TemperatureDBFacade facade = new TemperatureDBFacade();
	private HTTPTemperatureRequest httpTemperatureRequest = new HTTPTemperatureRequest();
	private TemperatureService temperatureService = new TemperatureService();

	public void start() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				Logger.info("started");
				long lastTransmittedInMillis = 0;
				List<Temperature> temperature;
				while (true) {
					try {
						temperature = temperatureService.getNewTemperature();
						// httpTemperatureRequest.postTemperature(temperature);
						if (System.currentTimeMillis() - lastTransmittedInMillis > TIME_BETWEEN_MEASUREMENTS
								&& temperature != null) {
							facade.persist(temperature);
							lastTransmittedInMillis = System.currentTimeMillis();
						}
						httpTemperatureRequest.postAllUnTransferedTemperatures();
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
				}
			}
		});

		t.start();
	}

}
