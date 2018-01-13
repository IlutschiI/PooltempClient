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
	
	public List<Temperature> persist(List<Temperature> temperatures) {
		transaction.begin();
		for (Temperature temperature : temperatures) {
			entityManager.persist(temperature);
		}
		transaction.commit();
		return temperatures;
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
	
	public void updateTransfered(Temperature t) {
		transaction.begin();
		int updatedEntities=entityManager.createNamedQuery(Temperature.SET_TRANSFERED_TRUE).setParameter("id", t.getId()).executeUpdate();
		transaction.commit();
		if(updatedEntities!=1) {
			throw new RuntimeException("More ore less than one Entity was updated!!!  updated entities: "+updatedEntities);
		}
		
	}
	
	public List<Temperature> findNotTransfered(){
		return entityManager.createNamedQuery(Temperature.FIND_NOT_TRANSFERED).setParameter("transfered", false).getResultList();
	}

}
