package at.pooltemp.client;

import java.util.Date;

import javax.persistence.EntityManager;

import at.pooltemp.client.db.EntityManagerFactory;
import at.pooltemp.client.service.temperature.TemperatureController;
import at.pooltemp.client.service.temperature.model.Temperature;

public class Main {

	public static void main(String[] args) {
		 new TemperatureController().start();

//		Temperature t = new Temperature();
//		t.setTemperature(12.2);
//		t.setTime(new Date());
//		EntityManager entityManager = EntityManagerFactory.getEntityManager();
//		entityManager.getTransaction().begin();
//		entityManager.persist(t);
//		entityManager.getTransaction().commit();
//
//		Temperature temp = (Temperature) entityManager.createNamedQuery(Temperature.FIND_ALL).getResultList().get(0);
//		System.out.println(temp);
//		System.exit(0);
	}

}
