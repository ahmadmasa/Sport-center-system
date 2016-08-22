package sport.center.terminal.jpa.repositories;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * @author Asendar
 *
 * @param <T>
 * @param <V>
 */
@MappedSuperclass
public abstract class MainRepository<T, V> implements DatabaseRepository<T, V> {
	/**
	 * 
	 */
	@Transient
	private Class<T> persistentClass;

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(MainRepository.class);

	/* (non-Javadoc)
	 * @see sport.center.terminal.jpa.repositories.DatabaseRepository#connect()
	 */
	public EntityManager connect() throws Exception {
		return JPAConfigurations.instance.getEntityManager();
	}

	/**
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MainRepository() {

		Class obtainedClass = getClass();

		Type genericSuperclass = null;
		for (;;) {
			genericSuperclass = obtainedClass.getGenericSuperclass();
			if (genericSuperclass instanceof ParameterizedType) {
				break;
			}
			obtainedClass = obtainedClass.getSuperclass();
		}
		ParameterizedType genericSuperclass_ = (ParameterizedType) genericSuperclass;

		persistentClass = ((Class) ((Class) genericSuperclass_.getActualTypeArguments()[0]));
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.jpa.repositories.DatabaseRepository#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		try {

			EntityManager entityManager = connect();
			List<T> data = entityManager.createQuery("from " + persistentClass.getSimpleName() + " t").getResultList();

			return data;

		} catch (Exception e) {
			logger.error("Exception : ", e);
		}

		return null;

	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.jpa.repositories.DatabaseRepository#find(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(V key) {
		try {

			EntityManager entityManager = connect();
			List<T> data = entityManager.createQuery("from " + persistentClass.getSimpleName() + " t where t.id= :id")
					.setParameter("id", key).getResultList();

			return data;

		} catch (Exception e) {
			logger.error("Exception : ", e);

		}

		return null;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.jpa.repositories.DatabaseRepository#saveAndFlush(java.lang.Object)
	 */
	@Override
	public T saveAndFlush(T entry) {
		try {
			EntityManager entityManager = connect();
			EntityTransaction transaction = entityManager.getTransaction();

			transaction.begin();
			entityManager.persist(entry);
			transaction.commit();
		} catch (Exception e) {
			logger.error("Exception : ", e);
		}

		return entry;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.jpa.repositories.DatabaseRepository#delete(java.lang.Object)
	 */
	@Override
	public T delete(T entry) {
		try {
			EntityManager entityManager = connect();
			EntityTransaction transaction = entityManager.getTransaction();

			transaction.begin();
			entityManager.remove(entry);
			transaction.commit();

		} catch (Exception e) {
			logger.error("Exception : ", e);

		}

		return entry;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.jpa.repositories.DatabaseRepository#update(java.lang.Object)
	 */
	@Override
	public T update(T entry) {
		try {
			EntityManager entityManager = connect();
			EntityTransaction transaction = entityManager.getTransaction();

			transaction.begin();
			entityManager.merge(entry);
			transaction.commit();

		} catch (Exception e) {
			logger.error("Exception : ", e);

		}

		return entry;
	}

}
