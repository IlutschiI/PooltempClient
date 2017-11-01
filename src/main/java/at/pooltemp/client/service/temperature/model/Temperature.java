package at.pooltemp.client.service.temperature.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({ @NamedQuery(name = Temperature.FIND_ALL, query = "Select t from Temperature t"),
		@NamedQuery(name = Temperature.FIND_BY_ID, query = "Select t from Temperature t where t.id = :id") })
@Entity
public class Temperature {
	public static final String FIND_ALL = "temperature.findAll";
	public static final String FIND_BY_ID = "temperature.findById";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Date time;
	private double temperature;
	private boolean transfered;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public boolean isTransfered() {
		return transfered;
	}

	public void setTransfered(boolean transfered) {
		this.transfered = transfered;
	}

}
