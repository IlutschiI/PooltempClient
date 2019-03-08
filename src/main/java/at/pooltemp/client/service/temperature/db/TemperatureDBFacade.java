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
		beginTransaction();
		entityManager.persist(temperature);
		closeTransaction();
		return temperature;
	}

	private void beginTransaction() {
		if (!transaction.isActive()) {
			transaction.begin();
		}
	}

	public List<Temperature> persist(List<Temperature> temperatures) {
		beginTransaction();
		for (Temperature temperature : temperatures) {
			entityManager.persist(temperature);
		}
		closeTransaction();
		return temperatures;
	}

	private void closeTransaction() {
		if (transaction.isActive() == true)
			transaction.commit();
	}

	public List<Temperature> findAll() {
		beginTransaction();
		List<Temperature> resultList = entityManager.createNamedQuery(Temperature.FIND_ALL).getResultList();
		closeTransaction();
		return resultList;
	}

	public Temperature findById(int id) {
		beginTransaction();
		Temperature result = null;
		try {
			result = (Temperature) entityManager.createNamedQuery(Temperature.FIND_BY_ID).setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
		} finally {
			closeTransaction();
		}
		return result;
	}

	public void updateTransfered(Temperature t) {
		beginTransaction();
		int updatedEntities = entityManager.createNamedQuery(Temperature.SET_TRANSFERED_TRUE)
				.setParameter("id", t.getId()).executeUpdate();
		closeTransaction();
		if (updatedEntities != 1) {
			throw new RuntimeException(
					"More ore less than one Entity was updated!!!  updated entities: " + updatedEntities);
		}

	}

	public List<Temperature> findNotTransfered() {
		return entityManager.createNamedQuery(Temperature.FIND_NOT_TRANSFERED).setParameter("transfered", false)
				.getResultList();
	}

}
