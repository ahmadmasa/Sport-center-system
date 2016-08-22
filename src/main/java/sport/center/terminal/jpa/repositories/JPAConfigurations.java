/**
 * 
 */
package sport.center.terminal.jpa.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sport.center.terminal.constants.Dialogs;

/**
 * @author Asendar
 *
 */

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class JPAConfigurations {

	/**
	 * 
	 */
	public static final JPAConfigurations instance = new JPAConfigurations();

	/**
	 * 
	 */
	private static final String PERSISTENCE_UNIT = "PersistenceUnit";
	/**
	 * 
	 */
	private static EntityManager entityManager = null;
	/**
	 * 
	 */
	private static EntityManagerFactory factory = null;

	/**
	 * @return
	 */
	public EntityManager getEntityManager() {
		try {

			if (entityManager == null)
				entityManager = connect();

		} catch (Exception e) {
			e.printStackTrace();
			Dialogs.showExceptionDialog(e);
		}
		return entityManager;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private EntityManager connect() throws Exception {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		entityManager = factory.createEntityManager();
		return entityManager;
	}

	/**
	 * 
	 */
	public void kill(){
		try {
			entityManager.close();
			
		} catch (Exception e) {
			System.err.println("Entity manager is not connected");
		}
		
		try {
			factory.close();
			
		} catch (Exception e) {
			System.err.println("Factory is not connected");
		}
		
		
		
		
	}
}
