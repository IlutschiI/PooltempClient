package at.pooltemp.client.db;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class EntityManagerFactory {

	private static final String PU_NAME = "pooltempPU";
	private static EntityManager em;

	public static EntityManager getEntityManager() {
		if (em == null)
			em = Persistence.createEntityManagerFactory(PU_NAME).createEntityManager();
		return em;
	}

}
