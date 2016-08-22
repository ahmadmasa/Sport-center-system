/**
 * 
 */
package sport.center.terminal.jpa.repositories;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import sport.center.terminal.jpa.entities.TerminalUtilsEntity;

/**
 * @author Asendar
 *
 */
public class TerminalUtilsRepository extends MainRepository<TerminalUtilsEntity, Long>{


	/**
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TerminalUtilsEntity getObject(long key) {
		try {

			EntityManager entityManager = JPAConfigurations.instance.getEntityManager();
			
			List<TerminalUtilsEntity> resultList = entityManager.createQuery("from "+TerminalUtilsEntity.class.getSimpleName()+" t where t.id= :id")
					.setParameter("id", key).getResultList();
			
			if(resultList==null || resultList.isEmpty()){
				java.sql.Date timeStamp = new java.sql.Date(new Date().getTime());
				TerminalUtilsEntity utilsObject = new TerminalUtilsEntity(1L, "theme_white", timeStamp,null);
				this.saveAndFlush(utilsObject);
				resultList.add(utilsObject);
			}
			
			
			
			return resultList.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
