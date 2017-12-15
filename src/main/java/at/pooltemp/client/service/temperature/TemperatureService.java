package at.pooltemp.client.service.temperature;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;

import at.pooltemp.client.service.temperature.model.Temperature;

public class TemperatureService {

	private Logger logger = Logger.getLogger("TemepratureController");
	private W1Master master = new W1Master();
	
	public Temperature getNewTemperature() {
		Temperature temperature = null;
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

		validateTemperature(temp);

		temperature.setTemperature(temp);
		temperature.setTime(new Date());
		return temperature;
	}

	private void validateTemperature(double temp) {
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
	}
	
}
