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

public class TemperatureController {

	private static final int MINS30_IN_MILLISECONDS = 1800;

	private W1Master master = new W1Master();
	private TemperatureDBFacade facade= new TemperatureDBFacade();
	private Temperature temperature;
	private Logger logger = Logger.getLogger("TemepratureController");
	private HTTPTemperatureRequest httpTemperatureRequest = new HTTPTemperatureRequest();

	public void start() {
		SimpleFormatter fmt = new SimpleFormatter();
		StreamHandler sh = new StreamHandler(System.out, fmt);
		logger.addHandler(sh);
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				logger.info("started");
				while (true) {
					try {
						temperature=getNewTemperature();
						facade.persist(temperature);
						//httpTemperatureRequest.postTemperature(temperature);
						httpTemperatureRequest.postAllUnTransferedTemperatures();
						Thread.sleep(MINS30_IN_MILLISECONDS);
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

	private Temperature getNewTemperature() {
		Temperature temperature=null;
		List<W1Device> devices = master.getDevices().stream()
				.filter(d -> d.getFamilyId() == TmpDS18B20DeviceType.FAMILY_CODE).collect(Collectors.toList());

		if (devices.isEmpty()) {
			logger.warning("no DS18B20 connected!!!");
		}

		for (W1Device w1Device : devices) {
			temperature = readTemperatureFromSensor(w1Device);
		}
		return temperature;
	}

	private Temperature readTemperatureFromSensor(W1Device w1Device) {
		Temperature temperature;
		temperature = new Temperature();

		double temp = ((TemperatureSensor) w1Device).getTemperature();

		if (temp == 85) {
			logger.warning("invalid Temperature was found");
			try {
				Thread.sleep(100);
				getNewTemperature();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			logger.info("valid Temperature was found, value is" + temp);
		}

		temperature.setTemperature(temp);
		temperature.setTime(new Date());
		return temperature;
	}
}
