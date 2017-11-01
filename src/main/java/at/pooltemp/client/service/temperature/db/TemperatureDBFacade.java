package at.pooltemp.client.service.temperature.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import at.pooltemp.client.db.EntityManagerFactory;
import at.pooltemp.client.service.temperature.model.Temperature;

public class TemperatureDBFacade {

	private EntityManager entityManager;
	EntityTransaction transaction;

	public TemperatureDBFacade() {
		entityManager = EntityManagerFactory.getEntityManager();
		transaction = entityManager.getTransaction();
	}

	public Temperature persist(Temperature temperature) {
		transaction.begin();
		entityManager.persist(temperature);
		transaction.commit();
		return temperature;
	}

	public List<Temperature> findAll() {
		transaction.begin();
		List<Temperature> resultList = entityManager.createNamedQuery(Temperature.FIND_ALL).getResultList();
		transaction.commit();
		return resultList;
	}

	public Temperature findById(int id) {
		transaction.begin();
		Temperature result = null;
		try {
			result = (Temperature) entityManager.createNamedQuery(Temperature.FIND_BY_ID).setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
		} finally {
			transaction.commit();
		}
		return result;
	}

}
