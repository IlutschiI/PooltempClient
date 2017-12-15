package at.pooltemp.client.service.temperature;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.stream.Collectors;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;

import at.pooltemp.client.http.HTTPTemperatureRequest;
import at.pooltemp.client.service.temperature.db.TemperatureDBFacade;
import at.pooltemp.client.service.temperature.model.Temperature;

public class PoolController {

	private static final int MINS30_IN_MILLISECONDS = 1800000;


	private TemperatureDBFacade facade = new TemperatureDBFacade();
	private Temperature temperature;
	private Logger logger = Logger.getLogger("TemepratureController");
	private HTTPTemperatureRequest httpTemperatureRequest = new HTTPTemperatureRequest();
	private TemperatureService temperatureService=new TemperatureService();

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
						facade.persist(temperature);
						// httpTemperatureRequest.postTemperature(temperature);
						if (System.currentTimeMillis() - lastTransmittedInMillis > MINS30_IN_MILLISECONDS) {
							httpTemperatureRequest.postAllUnTransferedTemperatures();
							lastTransmittedInMillis = System.currentTimeMillis();
						}
						logger.info("temperature = "+temperature.getTemperature());
						logger.info("waiting 1sec");
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
