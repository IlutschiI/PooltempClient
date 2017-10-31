package at.pooltemp.client.service.temperature.converter;

import at.pooltemp.client.service.temperature.model.Temperature;
import at.pooltemp.client.service.temperature.model.TemperatureDTO;

public class TemperatureConverter {

	public static TemperatureDTO convert(Temperature temperature) {
		TemperatureDTO temperatureDTO = new TemperatureDTO();
		temperatureDTO.setTemperature(temperature.getTemperature());
		temperatureDTO.setTime(temperature.getTime());
		return temperatureDTO;
	}

}
