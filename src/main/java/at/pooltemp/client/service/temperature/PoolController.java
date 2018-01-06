package at.pooltemp.client.service.temperature;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import at.pooltemp.client.http.HTTPTemperatureRequest;
import at.pooltemp.client.service.temperature.db.TemperatureDBFacade;
import at.pooltemp.client.service.temperature.model.Temperature;

public class PoolController {

	private static final int MINS30_IN_MILLISECONDS = 1800000;
	//private static final int MINS30_IN_MILLISECONDS = 60000;

	private TemperatureDBFacade facade = new TemperatureDBFacade();
	private Temperature temperature;
	private Logger logger = Logger.getLogger("TemepratureController");
	private HTTPTemperatureRequest httpTemperatureRequest = new HTTPTemperatureRequest();
	private TemperatureService temperatureService = new TemperatureService();

	public void start() {
		SimpleFormatter fmt = new SimpleFormatter();
		StreamHandler sh = new StreamHandler(System.out, fmt);
		logger.addHandler(sh);
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				logger.info("started");
				long lastTransmittedInMillis = 0;
				while (true) {
					try {
						temperature = temperatureService.getNewTemperature();
						// httpTemperatureRequest.postTemperature(temperature);
						if (System.currentTimeMillis() - lastTransmittedInMillis > MINS30_IN_MILLISECONDS
								&& temperature != null) {
							facade.persist(temperature);
							httpTemperatureRequest.postAllUnTransferedTemperatures();
							lastTransmittedInMillis = System.currentTimeMillis();
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		t.start();
	}

	public Temperature getTemperature() {
		return temperature;
	}

}
