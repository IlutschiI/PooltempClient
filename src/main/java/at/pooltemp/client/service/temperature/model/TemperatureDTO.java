package at.pooltemp.client.service.temperature.model;

import java.util.Date;

public class TemperatureDTO {

	private Date time;
	private double temperature;
	private TemperatureType temperatureType;

	public Date getTime() {
		return time;
	}

	public void setTime(Date date) {
		this.time = date;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public TemperatureType getTemperatureType() {
		return temperatureType;
	}

	public void setTemperatureType(TemperatureType temperatureType) {
		this.temperatureType = temperatureType;
	}

}
