package at.pooltemp.client.service.temperature;

import java.util.ArrayList;
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

	public List<Temperature> getNewTemperature() {
		List<Temperature> temperature = new ArrayList<>();
		List<W1Device> devices = master.getDevices().stream()
				.filter(d -> d.getFamilyId() == TmpDS18B20DeviceType.FAMILY_CODE).collect(Collectors.toList());

		if (devices.isEmpty()) {
			logger.warning("no DS18B20 connected!!!");
		}

		for (W1Device w1Device : devices) {
			temperature.add(readTemperatureFromSensor(w1Device));
		}
		return temperature;
	}

	private Temperature readTemperatureFromSensor(W1Device w1Device) {
		Temperature temperature;
		temperature = new Temperature();

		double temp = ((TemperatureSensor) w1Device).getTemperature();

		if (!isTemperatureValid(temp)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return readTemperatureFromSensor(w1Device);
		}

		temperature.setTemperature(temp);
		temperature.setTime(new Date());
		temperature.setSensorID(w1Device.getId());
		return temperature;
	}

	private boolean isTemperatureValid(double temp) {
		if (temp == 85) {
			return false;
		}
		return true;
	}

}
